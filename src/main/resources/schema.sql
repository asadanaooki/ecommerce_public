-- ======================


-- 1) テーブル削除
-- ======================
SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS favorite;
DROP TABLE IF EXISTS review;
SET FOREIGN_KEY_CHECKS=1;

-- user
CREATE TABLE `user` (
    user_id          CHAR(36)     NOT NULL,
    created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id)
);

-- product
CREATE TABLE product (
    product_id          CHAR(36)       NOT NULL,
    product_name        VARCHAR(100)   NOT NULL,
    price               INT            NOT NULL,
    product_description VARCHAR(1000)  NOT NULL,
    stock               INT            NOT NULL,
    sale_status         CHAR(1)        NOT NULL,
    created_at          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (product_id)
);

-- favorite
CREATE TABLE favorite (
    user_id             CHAR(36)       NOT NULL,
    product_id          CHAR(36)   NOT NULL,
    created_at          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, product_id),
    CONSTRAINT fk_favorite_user
      FOREIGN KEY(user_id)  REFERENCES user(user_id),
    CONSTRAINT fk_favorite_product
      FOREIGN KEY(product_id)  REFERENCES product(product_id)
);

CREATE TABLE review (
    user_id             CHAR(36)       NOT NULL,
    product_id          CHAR(36)   NOT NULL,
    rating           INT           NOT NULL,
    comment          VARCHAR(500),
    created_at          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, product_id),
    CONSTRAINT fk_review_user
      FOREIGN KEY(user_id)  REFERENCES user(user_id),
    CONSTRAINT fk_review_product
      FOREIGN KEY(product_id)  REFERENCES product(product_id)
);

