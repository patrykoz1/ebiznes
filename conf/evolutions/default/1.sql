# --- !Ups

CREATE TABLE "category" (
                            "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                            "name" VARCHAR NOT NULL,
                            "description" VARCHAR NOT NULL,
);
CREATE TABLE "comment" (
                            "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                            "body" VARCHAR NOT NULL,
                            "productId" INTEGER NOT NULL,
                            "customerId" INTEGER NOT NULL,

);
CREATE TABLE "invoice" (
                           "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                           "orderId" INTEGER NOT NULL,
                           "customerId" INTEGER NOT NULL,
                           "typeOf" INTEGER NOT NULL,

);
CREATE TABLE "order" (
                           "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                           "productId" INTEGER NOT NULL,
                           "customerId" INTEGER NOT NULL,

);
CREATE TABLE "payment" (
                         "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                         "typeOf" INTEGER NOT NULL,

);

CREATE TABLE "product" (
                           "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                           "name" VARCHAR NOT NULL,
                           "description" TEXT NOT NULL,
                           "categoryId" INT NOT NULL,
);
CREATE TABLE "purchase" (
                           "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                           "orderId" INTEGER NOT NULL,
                           "customerId" INTEGER NOT NULL,

);
CREATE TABLE "rate" (
                            "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                            "productId" INTEGER NOT NULL,
                            "customerId" INTEGER NOT NULL,
                            "grade" INTEGER NOT NULL,


);
CREATE TABLE "shipping" (
                        "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                        "order" INTEGER NOT NULL,
                        "typeOf" INTEGER NOT NULL,
);
CREATE TABLE "user" (
                            "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                            "firstName" VARCHAR NOT NULL,
                            "secondName" VARCHAR NOT NULL,
                            "email" VARCHAR NOT NULL,
                            "password" VARCHAR NOT NULL,
                            );

# --- !Downs

DROP TABLE "category"
DROP TABLE "comment"
DROP TABLE "invoice"
DROP TABLE "order"
DROP TABLE "payment"
DROP TABLE "product"
DROP TABLE "purchase"
DROP TABLE "rate"
DROP TABLE "shipping"
DROP TABLE "user"