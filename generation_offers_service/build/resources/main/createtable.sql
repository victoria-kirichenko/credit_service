CREATE TABLE response_type (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

INSERT INTO response_type (name) VALUES ('DECLINED'), ('ACCEPTED'), ('ASK_LATER');

CREATE TABLE user_status (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

INSERT INTO user_status (name) VALUES ('ACTIVE'), ('INACTIVE'), ('BANNED');

CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash CHAR(64) NOT NULL, -- Предполагая хэш SHA256
    creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_login TIMESTAMP WITHOUT TIME ZONE,
    status INTEGER NOT NULL,
	FOREIGN KEY (status) REFERENCES user_status (id) ON DELETE CASCADE
);

CREATE TABLE offers (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    content TEXT NOT NULL,
    creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE responses (
    id UUID PRIMARY KEY,
    offer_id UUID NOT NULL,
    response_type INTEGER NOT NULL,
    response_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (offer_id) REFERENCES offers (id) ON DELETE CASCADE
);