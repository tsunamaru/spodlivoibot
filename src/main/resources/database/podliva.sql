PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS chats (id INTEGER PRIMARY KEY AUTOINCREMENT,chat_name varchar(50),chat_id int(11) NOT NULL);
CREATE TABLE IF NOT EXISTS users (
  id integer NOT NULL PRIMARY KEY  AUTOINCREMENT,
  user_name varchar(50),
  user_id integer NOT NULL,
  chat integer DEFAULT NULL,
  FOREIGN KEY (`chat`) REFERENCES `chats` (`id`)
);
CREATE TABLE IF NOT EXISTS `dicks` (
  id integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  user integer NOT NULL,
  size integer NOT NULL,
  last_measurement datetime NOT NULL,
  FOREIGN KEY (`user`) REFERENCES `users` (`id`)
);
COMMIT;
