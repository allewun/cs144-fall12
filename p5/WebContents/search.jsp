<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<!DOCTYPE html>
<html>
    <head>
        <title>eBay Keyword Search Results</title>
        <script type="text/javascript" src="/eBay/js/autosuggest2.js"></script>
        <script type="text/javascript" src="/eBay/js/suggestions2.js"></script>
        <script type="text/javascript">
            window.onload = function () {
                var oTextbox = new AutoSuggestControl(document.getElementById("searchField"), new SearchSuggestions());
                    };
        </script>
        <link rel="stylesheet" type="text/css" href="/eBay/css/styles.css" />
    </head>
    <body>
        <div id="mini">
            <div id="header">
                <h1><span class="ebay"><span class="e">e</span><span class="b">B</span><span class="a">a</span><span class="y">y</span></span> Keyword Search Results</h1>
            </div>
            <form method="GET" action="/eBay/search">
                <input type="text" name="q" id="searchField" autocomplete="off" />
                <input type="hidden" name="numResultsToSkip" value="0" />
                <input type="hidden" name="numResultsToReturn" value="10" />
                <input type="submit" value="Search Again" />
            </form>
        </div>

        <div id="results">
            <h2>Basic Search Query: <em><%= request.getParameter("q") %></em></h2>
            <h2>Received <%= request.getAttribute("numResults") %> results</h2>
            <ul>
            <% SearchResult[] basicResults = (SearchResult[])request.getAttribute("basicResults");
               for (int i = 0; i < basicResults.length; ++i) {
                   String itemId = basicResults[i].getItemId();
                   String name = basicResults[i].getName();
                   out.println("<li><a href=\"/eBay/item?id=" + itemId + "\">" + itemId + "</a>: " + name + "</li>\n");
               }
            %>
            </ul>
            <div id="navlinks">
                <%
                if (request.getAttribute("prevLink") != null) {
                    out.println("<a class=\"button\" href=\"" + request.getAttribute("prevLink") + "\">&laquo; Prev</a>");
                }
                if (request.getAttribute("nextLink") != null) {
                    out.println("<a class=\"button\" href=\"" + request.getAttribute("nextLink") + "\">Next &raquo;</a>");
                } %>
            </div>
        </div>
    </body>
</html>
