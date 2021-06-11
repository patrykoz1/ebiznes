# --- !Ups

CREATE TABLE "category"
(
                            "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                            "name" VARCHAR NOT NULL,
                            "description" VARCHAR NOT NULL
);
INSERT INTO "category"("name","description") VALUES("diver","do nurkowania!");
INSERT INTO "category"("name","description") VALUES("garniturowiec","na wyjścia");

CREATE TABLE "comment"
(
                            "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                            "body" VARCHAR NOT NULL,
                            "productId" INTEGER NOT NULL,
                            "customerId" INTEGER NOT NULL

);

CREATE TABLE "invoice" (
                           "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                           "orderId" INTEGER NOT NULL,
                           "customerId" INTEGER NOT NULL,
                           "typeOf" INTEGER NOT NULL

);

CREATE TABLE "order"
(
                           "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                           "productId" INTEGER NOT NULL,
                           "customerId" INTEGER NOT NULL

);

CREATE TABLE "payment"
(
                         "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                         "typeOf" INTEGER NOT NULL

);

CREATE TABLE "product"
(
                           "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                           "name" VARCHAR NOT NULL,
                           "description" TEXT NOT NULL,
                           "categoryId" INT NOT NULL
);
INSERT INTO "product"("name","description","categoryId") VALUES("Seiko sea urchin","do nurkowania!",1);
INSERT INTO "product"("name","description","categoryId") VALUES("Invicta","fajny diver",1);


CREATE TABLE "purchase"
(
                           "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                           "orderId" INTEGER NOT NULL,
                           "customerId" INTEGER NOT NULL

);

CREATE TABLE "rate"
(
                            "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                            "productId" INTEGER NOT NULL,
                            "customerId" INTEGER NOT NULL,
                            "grade" INTEGER NOT NULL


);

CREATE TABLE "shipping"
(
                        "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                        "order" INTEGER NOT NULL,
                        "typeOf" INTEGER NOT NULL
);

CREATE TABLE "user"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "providerId"  VARCHAR NOT NULL,
    "providerKey" VARCHAR NOT NULL,
    "email"       VARCHAR NOT NULL
);

CREATE TABLE "authToken"
(
    "id"     INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "userId" INT     NOT NULL,
    FOREIGN KEY (userId) references user (id)
);

CREATE TABLE "passwordInfo"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "providerId"  VARCHAR NOT NULL,
    "providerKey" VARCHAR NOT NULL,
    "hasher"      VARCHAR NOT NULL,
    "password"    VARCHAR NOT NULL,
    "salt"        VARCHAR
);

CREATE TABLE "oAuth2Info"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "providerId"  VARCHAR NOT NULL,
    "providerKey" VARCHAR NOT NULL,
    "accessToken" VARCHAR NOT NULL,
    "tokenType"   VARCHAR,
    "expiresIn"   INTEGER
);
# --- !Downs

DROP TABLE "category";
DROP TABLE "comment";
DROP TABLE "invoice";
DROP TABLE "order";
DROP TABLE "payment";
DROP TABLE "product";
DROP TABLE "purchase";
DROP TABLE "rate";
DROP TABLE "shipping";
DROP TABLE "user";
DROP TABLE "authToken";
DROP TABLE "passwordInfo";
DROP TABLE "oAuth2Info";

DELETE FROM "category" WHERE name="diver";
DELETE FROM "category" WHERE name="garniturowiec";

DELETE FROM "product" WHERE name="Seiko sea urchin";
DELETE FROM "product" WHERE name="Invicta";

