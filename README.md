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
MySQL 5.5 and Java JDK 1.7 and an internet connection.

Installation
------------
Before running the program for the first time, you need to create a database first 
and then make a copy of the file <code>bourgogne/src/resources/META-INF/persistence_vanilla.xml</code>
in the same location and name it <code>persistence.xml</code>. In <code>persistence.xml</code>, change database, user and 
password accordingly to your installation.

You can either create the table by running <code>mysql yourdatabase -u root -p &lt; bourgogne.sql</code> (preferred solution)
or by letting the program create it during its first run. But you will need then to create
different indexes for better performance for future use:  

<code>CREATE INDEX datasetId_idx ON entities(datasetId);</code>  
<code>CREATE INDEX originalUID_idx ON entities(originalUID);</code>  
<code>CREATE INDEX updated_idx ON entities(updated DESC);</code>  
<code>CREATE INDEX deleted_idx ON entities(datasetid, deleted);</code>  
<code>CREATE INDEX processed_idx ON entities(datasetid, processed);</code>  
  
<code>CREATE INDEX dt_idx ON transactions(dt DESC);</code>  
<code>CREATE INDEX entityId_idx ON transactions(entityId);</code>  
<code>CREATE INDEX coordinates_idx ON transactions(coordinates);</code>  
<code>CREATE INDEX coll_inst_idx ON transactions(collectioncode, institutioncode);</code>  
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