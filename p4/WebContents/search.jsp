<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<!DOCTYPE html>
<html>
	<head>
		<title>eBay Keyword Search Results</title>
	</head>
	<body>
		<h1>eBay Keyword Search Results</h1>
        <h2>Basic Search Query: <%= request.getParameter("q") %></h2>
        <h2>Received <%= request.getAttribute("numResults") %> results</h2>
        <ul>
		<% SearchResult[] basicResults = (SearchResult[])request.getAttribute("basicResults");
		   for (int i = 0; i < basicResults.length; ++i) {
		       out.println("<li>" + basicResults[i].getItemId() + ": " + basicResults[i].getName() + "</li>");
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
