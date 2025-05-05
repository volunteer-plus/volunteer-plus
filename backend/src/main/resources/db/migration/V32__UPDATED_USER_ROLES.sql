CREATE TABLE roles
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    `create_date`     datetime default null,
    `update_date`     datetime default null
);


CREATE TABLE privileges
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_date     datetime default null,
    update_date     datetime default null
);


CREATE TABLE user_roles
(
    user_id     BIGINT    NOT NULL,
    role_id     BIGINT    NOT NULL,
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,
    CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);


CREATE TABLE role_privileges
(
    role_id      BIGINT    NOT NULL,
    privilege_id BIGINT    NOT NULL,
    granted_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (role_id, privilege_id),
    CONSTRAINT fk_rp_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE,
    CONSTRAINT fk_rp_priv FOREIGN KEY (privilege_id) REFERENCES privileges (id) ON DELETE CASCADE
);
