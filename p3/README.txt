
CS 144 - Fall '12
Project 3

Partners:
    * Samuel Jun (003-955-212 - samuel.h.jun@gmail.com)
    * Allen Wu   (103-790-579 - allen.wu@ucla.edu)


Part A.2 - Decide Indices to Create

    Create MySQL indices on:
        Items.itemID (automatically created)
        Items.sellerID
        Items.buy_price
        Items.ends
        Bids.userID

    Create Lucene indices on:
        Items.name
        Categories.categories (concatenation of all categories)
        Items.description

        Items.name + Categories.categories + Items.description

    The MYSQL indices are used for the advanced search function. The first three
    indices of the Lucene indices are also for the advanced search function. The
    fourth and last index is used for the basic search function.
    Please note that the second Lucene index is a concatenation of all the
    categories for a unique itemID. The SQL tables are set up so that there is a
    Categories table containing tuples with an itemID and a single category.
    Each category is listed with its corresponding itemID.
