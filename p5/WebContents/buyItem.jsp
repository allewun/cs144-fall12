<!DOCTYPE html>
<html>
    <head>
        <title>Buy Item</title>
        <link rel="stylesheet" type="text/css" href="/eBay/css/styles.css" />
    </head>
    <body>
        <div id="mini">
            <div id="header">
                <h1><span class="ebay"><span class="e">e</span><span class="b">B</span><span class="a">a</span><span class="y">y</span></span> Buy Item</h1>
            </div>
        </div>

        <div id="results">
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
            </table>

            <form method="post" action="https://<%= request.getServerName() %>:8443<%= request.getContextPath() %>/confirmation">
                Credit Card Number: <input type="text" name="creditCardNumber" /><br />
                <input type="submit" value="Submit" />
            </form>

            <table>
                <tr>
                    <td>getServerName()</td>
                    <td><%= request.getServerName() %></td>
                </tr>
                <tr>
                    <td>getContextPath()</td>
                    <td><%= request.getContextPath() %></td>
                </tr>
                <tr>
                    <td>isSecure()</td>
                    <td><%= request.isSecure() %></td>
                </tr>
            </table>
        </div>
    </body>
</html>
