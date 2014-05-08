
# #############################################################################
# -----------------------------------------------------------------------------
# ------------------------ Topicos_Collector ----------------------------------
# -----------------------------------------------------------------------------
# #############################################################################

DROP DATABASE IF EXISTS topicos_collector;

CREATE DATABASE IF NOT EXISTS topicos_collector;
USE topicos_collector;

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

 
# #############################################################################
# -----------------------------------------------------------------------------
# ------------------------ Topicos_Indexador ----------------------------------
# -----------------------------------------------------------------------------
# #############################################################################

DROP DATABASE IF EXISTS topicos_indexador;

CREATE DATABASE IF NOT EXISTS topicos_indexador;
USE topicos_indexador;

# -----------------------------------------------------------------------------
# -----------------------------------------------------------------------------
#       TABLE : words
# -----------------------------------------------------------------------------
# -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS words
 (    
    word_id BIGINT( 4 ) UNSIGNED NOT NULL AUTO_INCREMENT,
    word_value TEXT CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL,
    word_idf FLOAT NULL,
    PRIMARY KEY (word_id) 
 );
 

# -----------------------------------------------------------------------------
# -----------------------------------------------------------------------------
#       TABLE : documents
# -----------------------------------------------------------------------------
# -----------------------------------------------------------------------------
# peut etre pas besoim de recreer cette table 
# on peut reprendre les pages du colectionneur directement 

CREATE TABLE IF NOT EXISTS documents
 (
    document_id BIGINT( 4 ) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    document_name TEXT NOT NULL,
    document_url  TEXT NOT NULL,
    document_title  TEXT NOT NULL
 );


# -----------------------------------------------------------------------------
# -----------------------------------------------------------------------------
#       TABLE : pair
# -----------------------------------------------------------------------------
# -----------------------------------------------------------------------------


CREATE TABLE IF NOT EXISTS pair
 (  
    pair_word_id BIGINT( 4 ) UNSIGNED NOT NULL,
    pair_document_id BIGINT( 4 ) UNSIGNED NOT NULL,
    pair_frequency BIGINT( 4 ) NULL,
    pair_tf FLOAT NULL,
    pair_w FLOAT NULL,
    pair_r FLOAT NULL,  
    FOREIGN KEY (pair_word_id) REFERENCES words(word_id),
    FOREIGN KEY (pair_document_id) REFERENCES Documents(document_id)
 );
 