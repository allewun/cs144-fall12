package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.RequestDispatcher;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;
import java.io.StringReader;
import org.xml.sax.InputSource;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.xpath.XPathConstants;
import java.util.Vector;

public class ItemServlet extends HttpServlet implements Servlet {

    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try {
            AuctionSearchClient as = new AuctionSearchClient();

            String itemId = request.getParameter("id");
            String itemXML = as.getXMLDataForItemId(itemId);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(itemXML)));
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = null;

            // Item/Name
            expr = xpath.compile("Item/Name");
            String itemName = expr.evaluate(doc);
            request.setAttribute("itemName", itemName);
            // Item/Category
            expr = xpath.compile("Item/Category");
            NodeList itemCategoryNodes = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
            Vector itemCategoriesVector = new Vector();
            for (int i = 0; i < itemCategoryNodes.getLength(); i++) {
                Node currentNode = itemCategoryNodes.item(i);
                itemCategoriesVector.add(currentNode.getTextContent());
            }
            String[] itemCategories = new String[itemCategoriesVector.size()];
            itemCategoriesVector.toArray(itemCategories);
            //request.setAttribute("itemCategories", itemCategories);
            // Item/Currently
            expr = xpath.compile("Item/Currently");
            String itemCurrently = expr.evaluate(doc);
            request.setAttribute("itemCurrently", itemCurrently);
            // Item/Buy_Price 
            if (itemXML.contains("<Buy_Price>"))
            {
                expr = xpath.compile("Item/Buy_Price");
                String itemBuyPrice = expr.evaluate(doc);
                request.setAttribute("itemBuyPrice", itemBuyPrice);
            }
            // Item/First_Bid
            expr = xpath.compile("Item/First_Bid");
            String itemFirstBid = expr.evaluate(doc);
            request.setAttribute("itemFirstBid", itemFirstBid);
            // Item/Number_of_Bids
            expr = xpath.compile("Item/Number_of_Bids");
            String itemNumberOfBids = expr.evaluate(doc);
            request.setAttribute("itemNumberOfBids", itemNumberOfBids);
            // Item/Bids
            //
            // Item/Location
            expr = xpath.compile("Item/Location");
            String itemLocation = expr.evaluate(doc);
            request.setAttribute("itemLocation", itemLocation);
            // Item/Country
            expr = xpath.compile("Item/Country");
            String itemCountry = expr.evaluate(doc);
            request.setAttribute("itemCountry", itemCountry);
            // Item/Started
            expr = xpath.compile("Item/Started");
            String itemStarted = expr.evaluate(doc);
            request.setAttribute("itemStarted", itemStarted);
            // Item/Ends
            expr = xpath.compile("Item/Ends");
            String itemEnds = expr.evaluate(doc);
            request.setAttribute("itemEnds", itemEnds);
            // Item/Seller@UserID
            expr = xpath.compile("Item/Seller/@UserID");
            String itemSellerUserID = expr.evaluate(doc);
            request.setAttribute("itemSellerUserID", itemSellerUserID);
            // Item/Seller@Rating
            expr = xpath.compile("Item/Seller/@Rating");
            String itemSellerRating = expr.evaluate(doc);
            request.setAttribute("itemSellerRating", itemSellerRating);

            // Item/Description
            expr = xpath.compile("Item/Description");
            String itemDescription = expr.evaluate(doc);
            request.setAttribute("itemDescription", itemDescription);


            request.getRequestDispatcher("/item.jsp").forward(request, response);
        }
        catch (ServletException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	    catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        }        

    }
}
