-- CS 144 - Fall '12
-- Project 3

-- buildSQLIndex.sql
--  Creates the SQL indices on tables in our project

USE CS144;

CREATE INDEX index_seller ON Items(seller);
CREATE INDEX index_buyPrice ON Items(buy_price);
CREATE INDEX index_ends ON Items(ends);
CREATE INDEX index_bidder on Bids(userID);
