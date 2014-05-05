DROP DATABASE IF EXISTS Topicos_Collector;

CREATE DATABASE IF NOT EXISTS Topicos_Collector;
USE Topicos_Collector;

# -----------------------------------------------------------------------------
#       TABLE : Pages
# -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pages
 (
   page_id BIGINT( 4 ) NOT NULL AUTO_INCREMENT,
   page_name TEXT CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
   page_content TEXT CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL,
   page_md5  TEXT NOT NULL,
   PRIMARY KEY (page_id) 
 );
