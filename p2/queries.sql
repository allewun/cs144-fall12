SELECT COUNT(*)
FROM User;

SELECT COUNT(*)
FROM User
WHERE BINARY location = 'New York';

SELECT COUNT(*)
FROM (
  SELECT COUNT(*)
  AS COUNT
  FROM category
  GROUP BY itemID
  )
WHERE COUNT = 4;

SELECT itemID
FROM Item
WHERE currently = (
    SELECT MAX(currently)
    FROM Item
    WHERE num_of_bids > 0
      AND ends > '2001-12-20 00:00:01');