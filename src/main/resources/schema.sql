CREATE TABLE IF NOT EXISTS event_publication (
    id UUID PRIMARY KEY NOT NULL,
    listener_id TEXT NOT NULL,
    event_type TEXT NOT NULL,
    serialized_event TEXT NOT NULL,
    publication_date TIMESTAMP WITH TIME ZONE NOT NULL,
    completion_date TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS user_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS whatsapp_user (
    id BIGINT PRIMARY KEY NOT NULL,
    public_id UUID NOT NULL UNIQUE,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(255) UNIQUE,
    image_url VARCHAR(256),
    last_seen TIMESTAMP NOT NULL,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS authority (
    name VARCHAR(50) PRIMARY KEY NOT NULL
);

CREATE TABLE IF NOT EXISTS user_authority (
    user_id BIGINT NOT NULL,
    authority_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, authority_name),
    FOREIGN KEY (authority_name) REFERENCES authority(name),
    FOREIGN KEY (user_id) REFERENCES whatsapp_user(id)
);

CREATE SEQUENCE IF NOT EXISTS conversation_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS conversation (
    id BIGINT PRIMARY KEY NOT NULL,
    public_id UUID NOT NULL UNIQUE,
    name VARCHAR(256) NOT NULL,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_conversation (
    user_id BIGINT NOT NULL,
    conversation_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, conversation_id),
    FOREIGN KEY (user_id) REFERENCES whatsapp_user(id),
    FOREIGN KEY (conversation_id) REFERENCES conversation(id)
);

CREATE SEQUENCE IF NOT EXISTS message_binary_content_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS message_binary_content (
    id BIGINT PRIMARY KEY NOT NULL,
    file OID NOT NULL,
    file_content_type VARCHAR(255) NOT NULL,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP
);

CREATE SEQUENCE IF NOT EXISTS message_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS message (
    id BIGINT PRIMARY KEY NOT NULL,
    public_id UUID NOT NULL UNIQUE,
    send_time TIMESTAMP NOT NULL,
    send_state VARCHAR(255) NOT NULL,
    text VARCHAR(1024) NOT NULL,
    type VARCHAR(50) NOT NULL,
    user_fk_sender BIGINT NOT NULL,
    conversation_fk BIGINT NOT NULL,
    message_binary_content_fk BIGINT,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    FOREIGN KEY (user_fk_sender) REFERENCES whatsapp_user(id),
    FOREIGN KEY (conversation_fk) REFERENCES conversation(id),
    FOREIGN KEY (message_binary_content_fk) REFERENCES message_binary_content(id)
);

INSERT INTO authority (name) VALUES ('ROLE_USER') ON CONFLICT (name) DO NOTHING;