item (itemID PRIMARY KEY, name, currently, buy_price, first_bid, num_of_bids, started, ended, sellerID, description)
category (itemID PRIMARY KEY, category)
bids (itemID PRIMARY KEY, userID, time, amount)
user (userID PRIMARY KEY, rating, location, country)

