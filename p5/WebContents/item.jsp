<!DOCTYPE html>
<html>
    <head>
        <title>Item Results</title>
        <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
        <script type="text/javascript" src="/eBay/js/ebay.js"></script>
        <script type="text/javascript">
            var geocoder;
            var map;

            function initializeMap() {
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

            window.onload = function () {
                initializeMap();
                addEvent("itemIdForm", "submit", function (evt) {validateItemId(evt);});
            };
        </script>
        <link rel="stylesheet" type="text/css" href="/eBay/css/styles.css" />
    </head>
    <body>
        <div id="mini">
            <div id="header">
                <h1><span class="ebay"><span class="e">e</span><span class="b">B</span><span class="a">a</span><span class="y">y</span></span> Item Results</h1>
            </div>

            <form method="GET" action="/eBay/item" id="itemIdForm">
                <label for="itemId">Item ID:</label>
                <input type="text" id="itemId" name="id" />
                <input type="submit" value="Lookup another item" />
            </form>
        </div>


        <div id="results">
            <table>
                <tr>
                    <td>Item ID:</td>
                    <td><%= request.getParameter("id") %></td>
                </tr>
                <tr>
                    <td>Name:</td>
                    <td><%= request.getAttribute("itemName") %></td>
                </tr>
                <tr>
                    <td>Categories:</td>
                    <td>
<%
                    String[] itemCategories = (String[])request.getAttribute("itemCategories");
                    for (int i = 0; i < itemCategories.length; i++) {
                        if (i < itemCategories.length - 1) {
                            out.println(itemCategories[i] + ", ");
                        }
                        else {
                            out.println(itemCategories[i]);
                        }
                    }
%>
                    </td>
                </tr>
                <tr>
                    <td>Currently:</td>
                    <td><%= request.getAttribute("itemCurrently") %></td>
                </tr>
                <tr>
                    <td>Buy Price:</td>
                    <td>
<%
                   String itemBuyPrice = (String)request.getAttribute("itemBuyPrice");
                   if (itemBuyPrice != null)
                       out.println(itemBuyPrice);
                   else
                       out.println("N/A");
%>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
<%
                    if (itemBuyPrice != null) {
                        out.println("<a href=\"/eBay/buyItem\">Buy Now</a>");
                    }
%>
                    </td>
                </tr>
                <tr>
                    <td>First Bid:</td>
                    <td><%= request.getAttribute("itemFirstBid") %></td>
                </tr>
                <tr>
                    <td>Number of Bids:</td>
                    <td><%= request.getAttribute("itemNumberOfBids") %></td>
                </tr>

                <tr>
                    <td>Bids:</td>
                    <td>
<%
                    String[][] itemBids = (String[][])request.getAttribute("itemBids");
                    if (itemBids != null) {
                        out.println("<ol>");
                        for (int i = 0; i < itemBids.length; i++) {
                            out.println("<li>");
                            out.println("<table>");
                            out.println("    <tr><td>UserID:</td><td>"   + itemBids[i][0] + "</td></tr>");
                            out.println("    <tr><td>Rating:</td><td>"   + itemBids[i][1] + "</td></tr>");
                            out.println("    <tr><td>Location:</td><td>" + itemBids[i][2] + "</td></tr>");
                            out.println("    <tr><td>Country:</td><td>"  + itemBids[i][3] + "</td></tr>");
                            out.println("    <tr><td>Time:</td><td>"     + itemBids[i][4] + "</td></tr>");
                            out.println("    <tr><td>Amount:</td><td>"   + itemBids[i][5] + "</td></tr>");
                            out.println("</table>");
                            out.println("</li>");
                        }
                        out.println("</ol>");
                    }
                    else {
                       out.println("No Bids");
                    }
%>
                    </td>
                </td>
                <tr>
                    <td>Location:</td>
                    <td><span id="itemLocation"><%= request.getAttribute("itemLocation") %></span></td>
                </td>
                <tr>
                    <td>Country:</td>
                    <td><%= request.getAttribute("itemCountry") %></td>
                </td>
                <tr>
                    <td>Started:</td>
                    <td><%= request.getAttribute("itemStarted") %></td>
                </td>
                <tr>
                    <td>Ends:</td>
                    <td><%= request.getAttribute("itemEnds") %></td>
                </td>
                <tr>
                    <td>Seller UserID:</td>
                    <td><%= request.getAttribute("itemSellerUserID") %></td>
                </td>
                <tr>
                    <td>Seller Rating:</td>
                    <td><%= request.getAttribute("itemSellerRating") %></td>
                </td>
                <tr>
                    <td>Description:</td>
                    <td>
<%
                    String itemDescription = (String)request.getAttribute("itemDescription");
                    if (itemDescription != "")
                        out.println(itemDescription);
                    else
                        out.println("No Description Provided");
%>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div id="map_canvas"></div>
                    </td>
                </tr>
            </table>
        </div>
    </body>
</html>
