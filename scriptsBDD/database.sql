
# #############################################################################
# -----------------------------------------------------------------------------
# ------------------------ Topicos_sri ----------------------------------
# -----------------------------------------------------------------------------
# #############################################################################

DROP DATABASE IF EXISTS topicos_collector;
DROP DATABASE IF EXISTS topicos_indexador;
DROP DATABASE IF EXISTS topicos_sri;

CREATE DATABASE IF NOT EXISTS topicos_sri;
USE topicos_sri;

# -----------------------------------------------------------------------------
# -----------------------------------------------------------------------------
#       TABLE : words
# -----------------------------------------------------------------------------
# -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS words
 (    
    word_id BIGINT( 4 ) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    word_value TEXT CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL,
    word_idf FLOAT NULL
 );
 

# -----------------------------------------------------------------------------
# -----------------------------------------------------------------------------
#       TABLE : documents
# -----------------------------------------------------------------------------
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS documents
 (
    document_id         BIGINT( 4 ) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    document_name       TEXT NOT NULL,
    document_url        TEXT NOT NULL,
    document_title      TEXT NOT NULL,
    document_content    TEXT NOT NULL,
    document_r_sum      FLOAT NULL,
    document_r_square   FLOAT NULL,
    document_similarity FLOAT NULL,
    document_md5        VARCHAR( 32 ) NOT NULL
 );


# -----------------------------------------------------------------------------
# -----------------------------------------------------------------------------
#       TABLE : pairs
# -----------------------------------------------------------------------------
# -----------------------------------------------------------------------------


CREATE TABLE IF NOT EXISTS pairs
 (  
    pair_word_id BIGINT( 4 ) UNSIGNED NOT NULL,
    pair_document_id BIGINT( 4 ) UNSIGNED NOT NULL,
    pair_frequency BIGINT( 4 ) NULL,
    pair_tf FLOAT NULL,
    pair_w FLOAT NULL,
 
    FOREIGN KEY (pair_word_id) REFERENCES words(word_id),
    FOREIGN KEY (pair_document_id) REFERENCES Documents(document_id)
 );
 