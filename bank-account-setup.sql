DROP USER bank CASCADE;

CREATE USER bank
IDENTIFIED BY p4ssw0rd
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp
QUOTA 10M ON users;

GRANT CONNECT TO bank;
GRANT RESOURCE TO bank;
GRANT CREATE SESSION TO bank;
GRANT CREATE TABLE TO bank;
GRANT CREATE VIEW TO bank;

conn bank/p4ssw0rd;

/************************************
Tables and sequences
************************************/

CREATE SEQUENCE user_id_seq;
CREATE TABLE user_account (
    u_id INT PRIMARY KEY,
    username VARCHAR2(20) UNIQUE NOT NULL,
    password VARCHAR2(20) NOT NULL,
    is_admin INT DEFAULT 0 NOT NULL -- 0 is a regular user, 1 is an admin
);

CREATE SEQUENCE account_id_seq;
CREATE TABLE bank_account(
    a_id INT PRIMARY KEY,
    a_type VARCHAR2(20) DEFAULT 'Checking' NOT NULL,
    balance DOUBLE PRECISION DEFAULT 0.00 NOT NULL,
    u_id INT REFERENCES user_account(u_id),
    is_active INT DEFAULT 1 NOT NULL
);


CREATE SEQUENCE transaction_id_seq;
CREATE TABLE transaction_log (
    t_id INT PRIMARY KEY,
    a_id INT REFERENCES bank_account(a_id),
    u_id INT REFERENCES user_account(u_id),
    action VARCHAR2(20) NOT NULL,
    amount DOUBLE PRECISION DEFAULT NULL, -- Will be null only if account is being opened or closed
    t_time TIMESTAMP NOT NULL
);

/*******************************************************
Data
*******************************************************/

INSERT INTO user_account (u_id, username, password, is_admin) 
    VALUES (USER_ID_SEQ.nextval, 'admin', 'p4ssw0rd', 1); -- Creates a default admin account

INSERT INTO user_account (u_id, username, password, is_admin) 
    VALUES (USER_ID_SEQ.nextval, 'testuser', 'p4ssw0rd', 0); -- Creates a default test user
commit;

