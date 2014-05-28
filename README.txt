Note: JPA doesn't handle index creation. It is important to create some indexes 
manually. So here it is the code:

CREATE INDEX datasetId_idx ON entities(datasetId);
CREATE INDEX originalUID_idx ON entities(originalUID);
CREATE INDEX updated_idx ON entities(updated DESC);
CREATE INDEX deleted_idx ON entities(datasetid, deleted);
CREATE INDEX processed_idx ON entities(datasetid, processed);

CREATE INDEX dt_idx ON transactions(dt DESC);
CREATE INDEX entityId_idx ON transactions(entityId);
CREATE INDEX coordinates_idx ON transactions(coordinates);
CREATE INDEX coll_inst_idx ON transactions(collectioncode, institutioncode);
