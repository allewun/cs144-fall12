<!DOCTYPE html>
<html>
	<head>
		<title>Item Results</title>
	</head>
	<body>
		<h1>Item Results</h1>
		ItemID: <%= request.getParameter("id") %> <br />
        ItemName: <%= request.getAttribute("itemName") %> <br />
        ItemDescription: <%= request.getAttribute("itemDescription") %> <br />
	</body>
</html>
