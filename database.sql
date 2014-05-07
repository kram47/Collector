DROP DATABASE IF EXISTS Topicos_Collector;

CREATE DATABASE IF NOT EXISTS Topicos_Collector;
USE Topicos_Collector;

# -----------------------------------------------------------------------------
#       TABLE : Pages
# -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pages
 (
   page_id BIGINT( 4 ) NOT NULL AUTO_INCREMENT,
   page_name VARCHAR( 255 ) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
   page_content TEXT CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL,
   page_md5  VARCHAR( 32 ) NOT NULL,
   PRIMARY KEY (page_id) 
 );

 
DROP DATABASE IF EXISTS Topicos_Indexador;

CREATE DATABASE IF NOT EXISTS Topicos_Indexador;
USE Topicos_Indexador;

# -----------------------------------------------------------------------------
#       TABLE : words
# -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS words
 (
   word_id BIGINT( 4 ) NOT NULL AUTO_INCREMENT,
   word_value TEXT CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL,
   word_idf  ,
   PRIMARY KEY (word_id) 
 );
 
# pas besoim de recreer cette table on peut reprendre les pages du colectionneur directement 
 # -----------------------------------------------------------------------------
#       TABLE : documents
# -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS documents
 (
   document_id BIGINT( 4 ) NOT NULL AUTO_INCREMENT,
   document_name TEXT CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL,
   document_url  TEXT CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL,
   PRIMARY KEY (document_id) 
 );
 