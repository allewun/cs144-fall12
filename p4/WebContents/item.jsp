<!DOCTYPE html>
<html>
	<head>
		<title>Item Results</title>
	</head>
	<body>
		<h1>Item Results</h1>
		ID: <%= request.getParameter("id") %> <br />
        Name: <%= request.getAttribute("itemName") %> <br />
        Category: <br />
        Currently: <%= request.getAttribute("itemCurrently") %> <br />
        First_Bid: <%= request.getAttribute("itemFirstBid") %> <br />
        Number_of_Bids: <%= request.getAttribute("itemNumberOfBids") %> <br />
        Bids: <br />
        Location: <%= request.getAttribute("itemLocation") %> <br />
        Country: <%= request.getAttribute("itemCountry") %> <br />
        Started: <%= request.getAttribute("itemStarted") %> <br />
        Ends: <%= request.getAttribute("itemEnds") %> <br />
        Seller UserID: <%= request.getAttribute("itemSellerUserID") %> <br />
        Seller Rating: <%= request.getAttribute("itemSellerRating") %> <br />
        Description: <%= request.getAttribute("itemDescription") %> <br />
	</body>
</html>
