<!DOCTYPE html>
<html>
    <head>
        <title>Item Results</title>
        <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
        <script type="text/javascript">
            var geocoder;
            var map;

            function initialize() {
                geocoder = new google.maps.Geocoder();
                var latlng = new google.maps.LatLng(39.977120, -101.345216);
                var myOptions = {
                    zoom: 3, // default is 8
                    center: latlng,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                };
                map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);

                setLocation();
            }
            function setLocation() {
                var address = document.getElementById("itemLocation").innerHTML;
                geocoder.geocode( {'address': address}, function(results, status) {
                    if (status == google.maps.GeocoderStatus.OK) {
                        map.setCenter(results[0].geometry.location);
                        map.setZoom(14);
                    }
                });
            }

            window.onload = function () {initialize();};
            </script>
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
                    out.println("            <li>Category: " + itemCategories[i] + "</li>");
                }
%>
            <li>Currently: <%= request.getAttribute("itemCurrently") %></li>
<%
               String itemBuyPrice = (String)request.getAttribute("itemBuyPrice");
               if (itemBuyPrice != null)
                   out.println("            <li>Buy Price: " + itemBuyPrice + "</li>");
               else
                   out.println("            <li>Buy Price: N/A</li>");
%>
            <li>First Bid: <%= request.getAttribute("itemFirstBid") %></li>
            <li>Number of Bids: <%= request.getAttribute("itemNumberOfBids") %></li>
            <li>
<%
                String[][] itemBids = (String[][])request.getAttribute("itemBids");
                if (itemBids != null) {
                    out.println("            Bids:");
                    out.println("            <ol>");
                    for (int i = 0; i < itemBids.length; i++) {
                        out.println("            <li>UserID: " + itemBids[i][0] + "<br />");
                        out.println("                Rating: " + itemBids[i][1] + "<br />");
                        out.println("                Location: " + itemBids[i][2] + "<br />");
                        out.println("                Country: " + itemBids[i][3] + "<br />");
                        out.println("                Time: " + itemBids[i][4] + "<br />");
                        out.println("                Amount: " + itemBids[i][5] + "<br /></li>");
                    }
                    out.println("            </ol>");
                }
                else {
                   out.println("            Bids: No Bids");
                }
%>
            </li>
            <li>Location: <span id="itemLocation"><%= request.getAttribute("itemLocation") %></span></li>
            <li>Country: <%= request.getAttribute("itemCountry") %></li>
            <li>Started: <%= request.getAttribute("itemStarted") %></li>
            <li>Ends: <%= request.getAttribute("itemEnds") %></li>
            <li>Seller UserID: <%= request.getAttribute("itemSellerUserID") %></li>
            <li>Seller Rating: <%= request.getAttribute("itemSellerRating") %></li>
            <li>Description:
<%
                String itemDescription = (String)request.getAttribute("itemDescription");
                if (itemDescription != "")
                    out.println(itemDescription);
                else
                    out.println("No Description Provided");
%>
            </li>
            <li>Map: <div id="map_canvas" style="width:400px; height:350px"></div></li>
        </ul>
    </body>
</html>
