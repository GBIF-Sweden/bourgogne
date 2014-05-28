bourgogne
=========

Purpose
-------
This program is creates and fill the data warehouse where biodiversity data is stored.

It downloads a DarwinCore Archive file from an IPT installation and saves it in a 
table. Each record is regarded as a document and successive versions of a record 
are kept. Thus it is always possible to find a record in the state it was when a
publication or a website referring to it was written.

Documents are stored as zipped JSON objects.

Requirement
-----------
MySQL and Java JDK 1.7 and an internet connection.

Installation
------------
Before running the program for the first time, you need to create a database first 
and then copy the file <code>bourgogne/src/resources/META-INF/persistence_vanilla.xml</code>
in the same location as <code>persistence.xml</code> and change database, user and 
password accordingly to your installation.

You can either create the table with running
<code>
mysql yourdatabase -u root -p <bourgogne.sql
</code>
or let the program create it during its first run. But you will need then to create
different indexes for better performance for future use:
<code>
CREATE INDEX datasetId_idx ON entities(datasetId);
CREATE INDEX originalUID_idx ON entities(originalUID);
CREATE INDEX updated_idx ON entities(updated DESC);
CREATE INDEX deleted_idx ON entities(datasetid, deleted);
CREATE INDEX processed_idx ON entities(datasetid, processed);

CREATE INDEX dt_idx ON transactions(dt DESC);
CREATE INDEX entityId_idx ON transactions(entityId);
CREATE INDEX coordinates_idx ON transactions(coordinates);
CREATE INDEX coll_inst_idx ON transactions(collectioncode, institutioncode);
</code>

Run
---
Copy the file <code>sample.xml</code> located at the root of this directory and
name it according to the resource you want to use. Then open it and change the
content for your needs.

On the command line:
<code>
java -jar bourgogne.jar resource.xml
</code>