
-- 1. Find the number of users in the database.
SELECT COUNT(*)
FROM Users;


-- 2. Find the number of sellers from "New York",
--    (i.e., sellers whose location is exactly the
--    string "New York"). Pay special attention to
--    case sensitivity. You should match the sellers
--    from "New York" but not from "new york".
SELECT COUNT(*)
FROM Users
WHERE BINARY location = 'New York';


-- 3. Find the number of auctions belonging to exactly
--    four categories.
SELECT COUNT(*)
FROM (
  SELECT COUNT(*)
  AS COUNT
  FROM Categories
  GROUP BY itemID
  ) C
WHERE COUNT = 4;


-- 4. Find the ID(s) of current (unsold) auction(s) with
--    the highest bid. Remember that the data was captured
--    at the point in time December 20th, 2001, one second
--    after midnight, so you can use this time point to decide
--    which auction(s) are current. Pay special attention to the
--    current auctions without any bid.
SELECT itemID
FROM Items
WHERE currently = (
    SELECT MAX(currently)
    FROM Items
    WHERE num_of_bids > 0
      AND ends > '2001-12-20 00:00:01')
  AND num_of_bids > 0
  AND ends > '2001-12-20 00:00:01';


-- 5. Find the number of sellers whose rating is higher than 1000.
SELECT COUNT(*)
FROM (
  SELECT DISTINCT userID
  FROM Users, Items
  WHERE Users.userID = Items.sellerID
    AND Users.rating > 1000
  ) U;


-- 6. Find the number of users who are both sellers and bidders.
SELECT COUNT(*)
FROM (
  SELECT DISTINCT sellerID
  FROM Items, Bids
  WHERE sellerID = userID
  ) U;


-- 7. Find the number of categories that include at least one item with a bid of more than $100.
SELECT COUNT(*)
FROM (
  SELECT DISTINCT category
  FROM Categories, Bids
  WHERE Categories.itemID = Bids.itemID
    AND Bids.amount > 100
  ) U;