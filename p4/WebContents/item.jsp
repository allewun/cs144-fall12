<!DOCTYPE html>
<html>
    <head>
        <title>Item Results</title>
    </head>
    <body>
        <h1>Item Results</h1>
        <form method="GET" action="/eBay/item">
			<label for="itemId">Item ID:</label>
			<input type="text" id="itemId" name="id" />
			<input type="submit" value="Lookup another item" />
		</form>
		<ul>
        	<li>ID: <%= request.getParameter("id") %></li>
        	<li>Name: <%= request.getAttribute("itemName") %></li>
            <%
               String[] itemCategories = (String[])request.getAttribute("itemCategories");
               for (int i = 0; i < itemCategories.length; i++) {
                out.println("        <li>Category: " + itemCategories[i] + "</li>");
               }
            %>
        	<li>Currently: <%= request.getAttribute("itemCurrently") %></li>
            <%
               String itemBuyPrice = (String)request.getAttribute("itemBuyPrice");
               if (itemBuyPrice != null)
                   out.println("        <li>Buy Price: " + itemBuyPrice + "</li>");
            %>
        	<li>First Bid: <%= request.getAttribute("itemFirstBid") %></li>
        	<li>Number of Bids: <%= request.getAttribute("itemNumberOfBids") %></li>
        	<li>
            <%
               String[][] itemBids = (String[][])request.getAttribute("itemBids");
               if (itemBids != null) {
                   out.println("        Bids:\n");
                   out.println("        <ol>");
                       for (int i = 0; i < itemBids.length; i++) {
                           out.println("        <li>UserID: " + itemBids[i][0] + "<br />");
                           out.println("            Rating: " + itemBids[i][1] + "<br />");
                           out.println("            Location: " + itemBids[i][2] + "<br />");
                           out.println("            Country: " + itemBids[i][3] + "<br />");
                           out.println("            Time: " + itemBids[i][4] + "<br />");
                           out.println("            Amount: " + itemBids[i][5] + "<br /></li>");
                        }
                   out.println("        </ol>");
               }
               else {
                  out.println("        Bids: No Bids");
               }
            %>
         	</li>
        	<li>Location: <%= request.getAttribute("itemLocation") %></li>
        	<li>Country: <%= request.getAttribute("itemCountry") %></li>
        	<li>Started: <%= request.getAttribute("itemStarted") %></li>
        	<li>Ends: <%= request.getAttribute("itemEnds") %></li>
        	<li>Seller UserID: <%= request.getAttribute("itemSellerUserID") %></li>
        	<li>Seller Rating: <%= request.getAttribute("itemSellerRating") %></li>
        	<li>Description: <%= request.getAttribute("itemDescription") %></li>
        </ul>
    </body>
</html>
