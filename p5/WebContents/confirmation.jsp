<!DOCTYPE html>
<html>
    <head>
        <title>Buy Confirmation</title>
        <link rel="stylesheet" type="text/css" href="/eBay/css/styles.css" />
    </head>
    <body>
        <div id="mini">
            <div id="header">
                <h1><span class="ebay"><span class="e">e</span><span class="b">B</span><span class="a">a</span><span class="y">y</span></span> Buy Confirmation</h1>
            </div>
        </div>

        <div id="result">
            <table>
                <tr>
                    <td>Item ID:</td>
                    <td><%= request.getAttribute("itemId") %></td>
                </tr>
                <tr>
                    <td>Item Name:</td>
                    <td><%= request.getAttribute("itemName") %></td>
                </tr>
                <tr>
                    <td>Buy Price:</td>
                    <td><%= request.getAttribute("itemBuyPrice") %></td>
                </tr>
                <tr>
                    <td>Credit Card #:</td>
                    <td><%= request.getAttribute("creditCardNumber") %></td>
                </tr>
                <tr>
                    <td>Date:</td>
                    <td><%= request.getAttribute("currentDate") %></td>
                </tr>
                <tr>
                    <td>Time:</td>
                    <td><%= request.getAttribute("currentTime") %></td>
                </tr>
            </table>
        </div>
    </body>
</html>
