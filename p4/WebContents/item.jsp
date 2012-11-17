<!DOCTYPE html>
<html>
    <head>
        <title>Item Results</title>
    </head>
    <body>
        <h1>Item Results</h1>
        ID: <%= request.getParameter("id") %> <br />
        Name: <%= request.getAttribute("itemName") %> <br />
        <%  String[] itemCategories = (String[])request.getAttribute("itemCategories");
            for (int i = 0; i < itemCategories.length; i++) {
                out.println("        Category: " + itemCategories[i] + "<br />");
            } %>
        Currently: <%= request.getAttribute("itemCurrently") %> <br />
        <%  String itemBuyPrice = (String)request.getAttribute("itemBuyPrice");
            if (itemBuyPrice != null)
                out.println("        Buy Price: " + itemBuyPrice + "<br />"); %>
        First Bid: <%= request.getAttribute("itemFirstBid") %> <br />
        Number of Bids: <%= request.getAttribute("itemNumberOfBids") %> <br />
        <%  String[][] itemBids = (String[][])request.getAttribute("itemBids");
            if (itemBids != null) {
                out.println("        Bids: <br />");
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
                out.println("        Bids: No Bids<br />");
            }
             %>
        Location: <%= request.getAttribute("itemLocation") %> <br />
        Country: <%= request.getAttribute("itemCountry") %> <br />
        Started: <%= request.getAttribute("itemStarted") %> <br />
        Ends: <%= request.getAttribute("itemEnds") %> <br />
        Seller UserID: <%= request.getAttribute("itemSellerUserID") %> <br />
        Seller Rating: <%= request.getAttribute("itemSellerRating") %> <br />
        Description: <%= request.getAttribute("itemDescription") %> <br />
    </body>
</html>
