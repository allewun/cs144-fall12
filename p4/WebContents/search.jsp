<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<!DOCTYPE html>
<html>
	<head>
		<title>eBay Keyword Search Results</title>
                <script type="text/javascript" src="/eBay/js/autosuggest2.js"></script>
                <script type="text/javascript" src="/eBay/js/suggestions2.js"></script>
                <script type="text/javascript">
                    window.onload = function () {
                        var oTextbox = new AutoSuggestControl(document.getElementById("searchField"), new StateSuggestions());
                    };
                </script>
                <link rel="stylesheet" type="text/css" href="/eBay/css/styles.css" />
	</head>
	<body>
		<h1>eBay Keyword Search Results</h1>
		<form method="GET" action="/eBay/search">
			<input type="text" name="q" id="searchField" />
			<input type="hidden" name="numResultsToSkip" value="0" />
			<input type="hidden" name="numResultsToReturn" value="10" />
			<input type="submit" value="Search Again" />
		</form>
        <h2>Basic Search Query: <%= request.getParameter("q") %></h2>
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
		<%
		if (request.getAttribute("prevLink") != null) {
			out.println("<a href=\"" + request.getAttribute("prevLink") + "\">Prev</a>");
		}
		if (request.getAttribute("nextLink") != null) {
			out.println("<a href=\"" + request.getAttribute("nextLink") + "\">Next</a>");
		} %>
	</body>
</html>
