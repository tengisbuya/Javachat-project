CREATE DATABASE javachat;
USE javachat;
CREATE TABLE user(
	pk_id int NOT NULL,
    name varchar(20) NOT NULL,
    password varchar(20) NOT NULL,
    firstname varchar(20),
    lastname varchar(20),
    PRIMARY KEY(pk_id)
);
CREATE TABLE friend(
	user1_id int NOT NULL,
    user2_id int NOT NULL,
    CONSTRAINT fk_user1 FOREIGN KEY (user1_id) REFERENCES user(pk_id)
    ON DELETE cascade ON UPDATE cascade,
    CONSTRAINT fk_user2 FOREIGN KEY (user2_id) REFERENCES user(pk_id)
    ON DELETE cascade ON UPDATE cascade
);
-- User1 is sender, user2 is receiver
CREATE TABLE chat(
	user1_id int NOT NULL,
    user2_id int NOT NULL,
    message varchar(50),
    send_date datetime,
    CONSTRAINT fk_chat_user1 FOREIGN KEY (user1_id) REFERENCES user(pk_id)
    ON DELETE cascade ON UPDATE cascade,
    CONSTRAINT fk_chat_user2 FOREIGN KEY (user2_id) REFERENCES user(pk_id)
    ON DELETE cascade ON UPDATE cascade
);

SELECT * FROM user where NOT pk_id=2;
INSERT INTO user
VALUES(1, 'asdasd', 'asd', 'ten', 'aaa', '111', '2001');
SELECT * FROM user;