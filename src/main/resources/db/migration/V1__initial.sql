CREATE TABLE polls(id text PRIMARY KEY,question text NOT NULL);
CREATE TABLE poll_options(poll_id text NOT NULL REFERENCES polls(id),code text NOT NULL,PRIMARY KEY(poll_id,code));
CREATE TABLE votes(poll_id text NOT NULL,voter text NOT NULL,option_code text NOT NULL,PRIMARY KEY(poll_id,voter),FOREIGN KEY(poll_id,option_code) REFERENCES poll_options(poll_id,code));
