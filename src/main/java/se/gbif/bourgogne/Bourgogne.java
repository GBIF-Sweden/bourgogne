/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.gbif.bourgogne;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.text.Archive;
import org.gbif.dwc.text.ArchiveField;
import org.gbif.dwc.text.ArchiveFile;
import org.gbif.dwc.text.StarRecord;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.lang3.math.NumberUtils;
import org.xml.sax.SAXException;
import se.gbif.bourgogne.models.Entities;
import se.gbif.bourgogne.models.Transactions;
import se.gbif.bourgogne.utilities.DwcArchive;
import se.gbif.bourgogne.utilities.Gz;
import se.gbif.bourgogne.utilities.Strings;
import se.gbif.bourgogne.utilities.Db;

/**
 *
 * @author korbinus
 */
public class Bourgogne {

    private static boolean datasetExists;
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static int datasetid;
    private static boolean control = false;
    private static boolean local = false;               // for using local file instead (evil thing for dev)
    private static Gson gson = new Gson();              // initialize it once
    private static Type bodyType = new TypeToken<HashMap<Integer, JsonObject>>() {
    }.getType();
    private static boolean hasError = false;
    private static int totalRolledBack = 0;
    private static int batchWriteSize = 1000;
    private static Dataset dataset;
    private static boolean replaceBody = false;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException, ParserConfigurationException, SAXException, MalformedURLException, IOException, XPathExpressionException, Exception {

        int count;
        Archive archive;

        // read config
        File f = null;
        if (args.length > 0) {
            System.out.println("Reading configuration for resource " + args[0]);
            f = new File(args[0]);
            
            if(args.length > 1 && args[1].equals("mode=replace")){
                replaceBody = true;
                System.out.println("WARNING: all current record bodies for this dataset will be erased and replaced by the new ones.");
            }
        } else {
            f = new File(Bourgogne.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        }

        Config config = new Config();
        config.read(f);
        datasetid = config.getId();

        // now grab the DarwinCore Archive
        URL source = new URL(config.getURI());

        System.out.println("Treating resource " + config.getName());

        DwcArchive dwcarchive;

        if (local) {
            dwcarchive = new DwcArchive("/home/korbinus/Downloads/dwca-dina.zip");
        } else {
            dwcarchive = new DwcArchive(source);
        }
        archive = dwcarchive.getArchive();

        /*
         * local database setup
         */
        emf = javax.persistence.Persistence.createEntityManagerFactory("bourgognePU");
        em = emf.createEntityManager();

        dataset = new Dataset(em, config.getId());
        
        //check if dataset already in database
        datasetExists = dataset.isPresent();
        if(datasetExists) {
            System.out.println("Dataset already present; updating and adding records.");
            count = dataset.prepare();
            System.out.println(count + " records prepared for processing.");
        } else {
            System.out.println("New dataset; inserting records.");
        }

        // start a new transaction
        em.getTransaction().begin();
        int i = 0;

        int smallestAddedId = 0;                // for dealing with an infamous bug which I think exists in JPA
        int addedId = 0;
        Entities entity;
        
        for (StarRecord rec : archive) {
            i++;
            entity = makeRecord(rec.core(), archive);
            if (entity != null) {
                if (datasetExists && entity.getAddedId() != null) {
                    addedId = entity.getAddedId();                          // should run faster than invoking getAddedId() all the time
                    if (smallestAddedId == 0) {                             // first run, we grab the first added_id
                        smallestAddedId = addedId;
                    } else if (addedId < smallestAddedId) {                 // otherwise we look which added_id is the smallest and keep it
                        smallestAddedId = addedId;
                    }
                }
                
                em.merge(entity);

                if (i % batchWriteSize == 0 && i > 0) {
                    System.out.println("Commiting batch of " + batchWriteSize + " records.");
                    System.out.println(i+" records treated so far.");
                    commitBatch();
                }
            }
        }
        // commit the remaining records
        System.out.println("Commiting the last records.");
        em.getTransaction().commit();
        
        System.out.println(i+" records treated.");
        
        // now mark records that are deleted
        if (datasetExists) {

            // hack for fixing the mysterious bug...
            if (smallestAddedId > 0) {
                fixJPAbug(smallestAddedId);
            }

            // save transactions for entities flagged as deleted
            try {
                em.getTransaction().begin();
                List<Entities> deleted = em.createQuery("SELECT e FROM Entities e WHERE e.datasetid = ?1 AND e.processed = ?2 AND e.deleted = ?3", Entities.class)
                        .setParameter(1, datasetid).setParameter(2, false).setParameter(3, false)
                        .setHint("eclipselink.read-only", true)
                        .getResultList();
                i = 0;
                PID uuid = new PID();               // we initialize once and use setUUID for reducing the HEAP
                for (Entities ent : deleted) {
                    i++;
                    Transactions deltran = buildTransactionRecord(ent, true);
                    uuid.setUUID(deltran.getEntityId());
                    System.out.println("Saving transaction marking " + uuid.toString() + " as deleted");
                    em.merge(deltran);

                    if (i % batchWriteSize == 0 && i > 0) {
                        commitBatch();
                    }
                }
                em.getTransaction().commit();
            } catch (NoResultException e) {
            }

            count = dataset.flagAsDeleted();
            System.out.println(count + " records flagged as deleted.");
        }
        if(hasError) {
            System.out.println("*** Important *** there were some errors in this dataset. Please check them in the log.");
            System.out.println(totalRolledBack+" records were NOT inserted in the database");
        }
        // remove temp directory with uncompressed data
        dwcarchive.clean();
    }

    private static Entities makeRecord(Record r, Archive archive) throws SQLException, IOException {

        boolean newRecord;

        HashMap<Integer, JsonObject> body = new HashMap<>();

        JsonObject dwcRecord = new JsonObject();        // darwincore record
        JsonObject properties = new JsonObject();
        ArchiveFile af = archive.getCore();             //don't forget extensions...

        String originalUID = r.id();                    // not provided as dwc:occurrenceID by GBIF's library
        for (ArchiveField f : af.getFields().values()) {
            try {
                String str = r.value(f.getTerm());
                // if the value is not empty
                if (str != null) {
                    str = Strings.unscramble(str);

                    String propertyName = f.getTerm().toString();

                    properties.addProperty(propertyName, str);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        if (properties.has("dwc:associatedReferences")) {
            String associatedReferences = properties.get("dwc:associatedReferences").getAsString();
            properties.remove("dwc:associatedReferences");
            properties.addProperty("dwc:associatedReferences", originalUID + "; " + associatedReferences);
        } else {
            properties.addProperty("dwc:associatedReferences", originalUID);
        }

        // clean horrible dwc:classs by replacing it with dwc:class
        if (properties.has("dwc:classs")) {
            properties.addProperty("dwc:class", properties.get("dwc:classs").getAsString());
            properties.remove("dwc:classs");
        }

        dwcRecord.add("dwr:SimpleDarwinRecord", properties);

        Entities entity = null;

        //if *dataset* already present, search for record
        if (datasetExists) {
            if(control) {
                System.out.println("Dataset already present; looking for " + originalUID);
            }

            Entities oldEntity = Db.getSingleObject(em, "Entities.findByOriginalUID", "originalUID", originalUID);

            if (oldEntity == null) {
                if(control) {
                    System.out.println("New record for existing dataset");
                }
                newRecord = true;
            } else {
                JsonObject lastDwC;
                int size;
                if(!replaceBody) {
                    
                    body = gson.fromJson(Gz.decompress(oldEntity.getBody()), bodyType);

                    // the size of the object provides the last version... 
                    // that's a wrong way of doing, that should be corrected to
                    // something more reliable
                    size = body.size();
                    if(control){
                        System.out.println("Number of versions for this darwincore record: " + size);
                    }
                    lastDwC = body.get(size);                    // get the last version

                    // get the UUID we previously created and add it to the new 
                    // JSON object before comparing the two objects
    //                dwcRecord.get("dwr:SimpleDarwinRecord").getAsJsonObject()
    //                        .addProperty("dwc:occurrenceID", lastDwC.get("dwr:SimpleDarwinRecord").getAsJsonObject().get("dwc:occurrenceID").getAsString());
                } else {
                    lastDwC = null;
                    size = 0;
                }
                PID oldId = new PID(oldEntity.getId());
                dwcRecord.get("dwr:SimpleDarwinRecord").getAsJsonObject()
                        .addProperty("dwc:occurrenceID", oldId.toString());
                
                // compare the data with the new content
                // we need to return an entity even if nothing is new, but with the flag processed set to true
                if (dwcRecord.equals(lastDwC)) {
                    if(control){
                        System.out.println("Same content as before; do nothing.");  // actually it's wrong we update processed
                    }
                    entity = buildEntityRecord(oldEntity.getId(), oldEntity.getAddedId(), originalUID, oldEntity.getBody());
                } else {
                    if(control) {
                        System.out.println("New version of existing record; updating.");
                    }
                    body.put(++size, dwcRecord);
                    entity = buildEntityRecord(oldEntity.getId(), oldEntity.getAddedId(), originalUID, Gz.compress(gson.toJson(body, bodyType)));
                    /* save transaction */
                    Transactions transactions = buildTransactionRecord(entity);
                    em.merge(transactions);
                }
                newRecord = false;
            }
        } else {
            if(control) {
                System.out.println("New record for new dataset");
            }
            newRecord = true;
        }

        if (newRecord) {
            // add a PID
            PID pid = new PID();
            dwcRecord.get("dwr:SimpleDarwinRecord").getAsJsonObject()
                    .addProperty("dwc:occurrenceID", pid.toString());
            // now create body
            body.put(1, dwcRecord);

            // create entity
            entity = buildEntityRecord(pid.toBytes(), originalUID, Gz.compress(gson.toJson(body, bodyType)));

            // save transaction
            Transactions transactions = buildTransactionRecord(entity);
            em.merge(transactions);

            // the two lines below are for control only
            if (control) {
                bodyControl(entity.getBody());
            }
        }
        return entity;
    }

    /**
     * Build an Entities record
     * 
     * @param pid
     * @param originalUID
     * @param body
     * @return 
     */
    private static Entities buildEntityRecord(byte[] pid, String originalUID, byte[] body) {
        return buildEntityRecord(pid, -1, originalUID, body);
    }

    /**
     * Build an Entities record
     * 
     * @param pid the persistent identifier for this object
     * @param addedId the database integer id
     * @param originalUID the original id in the triplet form (intitutionCode:collectionCode:catalogNumber)
     * @param body
     * @return the Entities object
     */
    private static Entities buildEntityRecord(byte[] pid, int addedId, String originalUID, byte[] body) {
        Entities entity = new Entities();
        if (addedId > -1) {
            entity.setAddedId(addedId);
        }
        entity.setOriginalUID(originalUID);
        entity.setId(pid);
        entity.setBody(body);
        entity.setProcessed(true);
        entity.setDeleted(false);
        entity.setDatasetid(datasetid);

        return entity;
    }

    private static Transactions buildTransactionRecord(Entities entity) throws IOException {
        return buildTransactionRecord(entity, false);
    }

    private static Transactions buildTransactionRecord(Entities entity, boolean deleted) throws IOException {

        Transactions transaction = new Transactions();

        HashMap<Integer, JsonObject> body = gson.fromJson(Gz.decompress(entity.getBody()), bodyType);

        // get the last darwincore record in the list
        JsonObject lastDwC = body.get(body.size());
        JsonObject SimpleDarwinRecord = lastDwC.getAsJsonObject("dwr:SimpleDarwinRecord");

        transaction.setEntityId(entity.getId());
        transaction.setDatasetId(entity.getDatasetid());

        // get collection code
        transaction.setCollectioncode(SimpleDarwinRecord.get("dwc:collectionCode").getAsString());

        // get institution code
        transaction.setInstitutioncode(SimpleDarwinRecord.get("dwc:institutionCode").getAsString());

        // get if coordinates
        if (SimpleDarwinRecord.has("dwc:decimalLatitude")
                && SimpleDarwinRecord.has("dwc:decimalLongitude")
                && NumberUtils.isNumber(SimpleDarwinRecord.get("dwc:decimalLatitude").getAsString())
                && NumberUtils.isNumber(SimpleDarwinRecord.get("dwc:decimalLongitude").getAsString())) {
            transaction.setCoordinates(true);
        } else {
            transaction.setCoordinates(false);
        }

        if (deleted) {
            transaction.setType("DELETED");
        }

        return transaction;
    }

    /**
     * Commit a batch of records
     */
    private static void commitBatch() throws SQLException{
        try {
        em.flush();
        em.clear();
        em.getTransaction().commit();
        // start a new transaction
        em.getTransaction().begin();
        } catch (PersistenceException e) {
            Throwable t = getLastThrowable(e);
            //fetching Internal Exception
            SQLException exxx = (SQLException) t;  //casting Throwable object to SQL Exception
//            System.out.println(exxx.getSQLState());
            if("23000".equals(exxx.getSQLState())) // Integrity constraint violation
            {
                hasError = true;
                em.getTransaction().rollback();
                System.out.println(batchWriteSize + " records rolled back!");
                em.getTransaction().begin();
                totalRolledBack += batchWriteSize;
            }

        }
    }
    
    /**
     * Courtesy of Rupesh Kumar Kushwaha's Blog
     * http://rupeshkushwaha.blogspot.in/2011/09/how-to-access-internal-exception-type.html
     * 
     * @param e
     * @return 
     */
    @SuppressWarnings("empty-statement")
    private static Throwable getLastThrowable(Exception e) {
        Throwable t = null;
        for(t = e.getCause(); t.getCause() != null; t = t.getCause());
        return t;
    } 
    /**
     * Display body as a JSON object for control
     * 
     * @param body
     * @throws IOException
     */
    private static void bodyControl(byte[] body) throws IOException {
        System.out.println("Control (decompressed): " + Gz.decompress(body));
    }
    
    /**
     * Fix an annoying bug assumed to be from JPA
     *
     * @param smallestAddedId
     */
    private static void fixJPAbug(int smallestAddedId) {
        System.out.println("Fix weird bug with smallest added_id");
        em.getTransaction().begin();
        em.createQuery("UPDATE Entities e SET e.processed = ?1 WHERE e.addedId = ?2 AND e.deleted = ?3")
                .setParameter(1, true).setParameter(2, smallestAddedId).setParameter(3, false)
                .executeUpdate();        
        em.getTransaction().commit();
    }
}
