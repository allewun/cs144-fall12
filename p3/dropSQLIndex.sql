-- CS 144 - Fall '12
-- Project 3

-- dropSQLIndex.sql
--  Removes the SQL indices on tables in our project

USE CS144;

DROP INDEX index_seller ON Items;
DROP INDEX index_buyPrice ON Items;
DROP INDEX index_ends ON Items;
DROP INDEX index_bidder ON Bids;
