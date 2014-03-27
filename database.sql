DROP DATABASE IF EXISTS Topicos_Collector;

CREATE DATABASE IF NOT EXISTS Topicos_Collector;
USE Topicos_Collector;

# -----------------------------------------------------------------------------
#       TABLE : Pages
# -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pages
 (
   page_id BIGINT(4) NOT NULL AUTO_INCREMENT,
   page_name varchar(255) NOT NULL,
   page_content text NOT NULL,
   page_md5 int(11) NULL,
   PRIMARY KEY (page_id) 
 );


INSERT INTO pages(page_name, page_content, page_md5) VALUES("1", "coucou les amirs c'est ma super page web", 89);
INSERT INTO pages(page_name, page_content, page_md5) VALUES("2", "coucou les amirs c'est ma super page web", 89);
INSERT INTO pages(page_name, page_content, page_md5) VALUES("3", "coucou les amirs c'est ma super page web", 89);
INSERT INTO pages(page_name, page_content, page_md5) VALUES("4", "coucou les amirs c'est ma super page web", 89);
INSERT INTO pages(page_name, page_content, page_md5) VALUES("5", "coucou les amirs c'est ma super page web", 89);
INSERT INTO pages(page_name, page_content, page_md5) VALUES("6", "coucou les amirs c'est ma super page web", 89);
INSERT INTO pages(page_name, page_content, page_md5) VALUES("7", "coucou les amirs c'est ma super page web", 89);
INSERT INTO pages(page_name, page_content, page_md5) VALUES("8", "coucou les amirs c'est ma super page web", 89);
INSERT INTO pages(page_name, page_content, page_md5) VALUES("9", "coucou les amirs c'est ma super page web", 89);
INSERT INTO pages(page_name, page_content, page_md5) VALUES("10", "coucou les amirs c'est ma super page web", 89);
