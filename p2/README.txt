
CS 144 - Fall '12
Project 2


Partners:
	* Samuel Jun (003-955-212 - samuel.h.jun@gmail.com)
	* Allen Wu   (103-790-579 - allen.wu@ucla.edu)


Part B:
	1. Relations:

		item (itemID PRIMARY KEY, name, currently, buy_price, first_bid, num_of_bids, started, ended, sellerID, description)
		category (itemID PRIMARY KEY, category PRIMARY KEY)
		bids (itemID PRIMARY KEY, userID PRIMARY KEY, time PRIMARY KEY, amount)
		user (userID PRIMARY KEY, rating, location, country)

	2. Completely nontrivial functional dependencies

		N/A

	3. Are the relations in BCNF?

		Yes.
