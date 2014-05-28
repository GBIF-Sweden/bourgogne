/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.gbif.bourgogne;

import javax.persistence.EntityManager;
import se.gbif.bourgogne.models.Entities;
import se.gbif.bourgogne.utilities.Db;

/**
 * Management of a complete dataset 
 * @author korbinus
 */
public class Dataset {
    
    private int id;
    private String URI;
    private String name;
    
    private EntityManager em; 

    public Dataset(){ 
    }
    
    public Dataset(EntityManager em, int id) {
        this.em = em;
        this.id = id;
    }

    public Dataset(EntityManager em, int id, String name, String uri) {
        this.id = id;
        this.name = name;
        this.URI = uri;
    }

    public String getURI() {
        return URI;
    }

    public String getName() {
        return name;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public void setName(String name) {
        this.name = name;
    }
        
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    /**
     * Check if a dataset is already present in Entities by checking if 
     * one records is present.
     * As this creates a database access it is advised to store the result in a
     * boolean variable.
     * 
     * @return true if present, false otherwise
     */
    public boolean isPresent() {
        Entities entity = Db.getSingleObject(em, "Entities.findByDatasetid", "datasetid", id);
        if (entity == null) {        
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Flag records as deleted
     * 
     * @return the number of records impacted
     */
    public int flagAsDeleted() {
        em.getTransaction().begin();

        // deleted records are the ones that are not processed (processed is false)
        int count = em.createQuery("UPDATE Entities e SET e.deleted = ?1 WHERE e.datasetid = ?2 AND e.processed = ?3 AND e.deleted = ?4")
                .setParameter(1, true).setParameter(2, id).setParameter(3, false).setParameter(4, false)
                .executeUpdate();

        em.getTransaction().commit();

        return count;
    }
    
    /**
     * Prepare dataset records for processing by flagging processed at true where
     * deleted is flagged as false.
     * 
     * @return the number of records impacted
     */
    public int prepare() {
        em.getTransaction().begin();
        int count = em.createQuery("UPDATE Entities e SET e.processed = ?1 WHERE e.datasetid = ?2 AND e.deleted = ?3")
                .setParameter(1, false).setParameter(2, id).setParameter(3, false)
                .executeUpdate();
        System.out.println("Controle: "+count+" enregistrements mis à jour");
        em.getTransaction().commit();
        
        return count;
    }    
    
    /**
     * Remove the complete dataset. Not intended for everyday use.
     * 
     * @return the number of records impacted
     */
    public int remove() {
        em.getTransaction().begin();
        int count = em.createQuery("DELETE FROM Entities e WHERE e.datasetid = ?1")
                .setParameter(1, id)
                .executeUpdate();
        em.getTransaction().commit();
        
        return count;
    }
}
