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
import org.w3c.dom.NamedNodeMap;
import javax.xml.xpath.XPathConstants;
import java.util.Vector;
import java.util.regex.Pattern;

public class ItemServlet extends HttpServlet implements Servlet {

    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String jspError = "/error.jsp";

        try {
            AuctionSearchClient as = new AuctionSearchClient();
            String jspDest = "/item.jsp";

            String itemId = request.getParameter("id");
            String itemXML = as.getXMLDataForItemId(itemId);

            Pattern nonNumbers = Pattern.compile("\\D");
            if (request.getParameter("id") != null && nonNumbers.matcher(itemId).find()) {
                jspDest = jspError;
            }
            else {
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
                request.setAttribute("itemCategories", itemCategories);
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
                if (itemXML.contains("<Bid>")) {
                    expr = xpath.compile("Item/Bids/Bid");
                    NodeList itemBidsNodes = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);

                    Vector<Vector<String>>itemBidsVector = new Vector<Vector<String>>();

                    for (int i = 0; i < itemBidsNodes.getLength(); i++) {
                        // Current Bid properties
                        expr = xpath.compile("Item/Bids/Bid[" + (i+1) + "]/Bidder/@UserID");
                        String bidderUserID = expr.evaluate(doc);
                        expr = xpath.compile("Item/Bids/Bid[" + (i+1) + "]/Bidder/@Rating");
                        String bidderRating = expr.evaluate(doc);
                        expr = xpath.compile("Item/Bids/Bid[" + (i+1) + "]/Bidder/Location");
                        String bidderLocation = expr.evaluate(doc);
                        expr = xpath.compile("Item/Bids/Bid[" + (i+1) + "]/Bidder/Country");
                        String bidderCountry = expr.evaluate(doc);
                        expr = xpath.compile("Item/Bids/Bid[" + (i+1) + "]/Time");
                        String time = expr.evaluate(doc);
                        expr = xpath.compile("Item/Bids/Bid[" + (i+1) + "]/Amount");
                        String amount = expr.evaluate(doc);

                        // [UserID, Rating, Location, Country, Time, Amount]
                        Vector<String> currentBidVector = new Vector<String>();
                        currentBidVector.add(bidderUserID);
                        currentBidVector.add(bidderRating);
                        currentBidVector.add(bidderLocation);
                        currentBidVector.add(bidderCountry);
                        currentBidVector.add(time);
                        currentBidVector.add(amount);

                        itemBidsVector.add(currentBidVector);
                    }

                    String[][] itemBids = new String[itemBidsVector.size()][6];
                    for (int i = 0; i < itemBidsVector.size(); i++) {
                        itemBidsVector.get(i).toArray(itemBids[i]);
                    }
                    request.setAttribute("itemBids", itemBids);
                }
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
            }

            request.getRequestDispatcher(jspDest).forward(request, response);
        }
        catch (ServletException e) {
            request.getRequestDispatcher(jspError).forward(request, response);
            e.printStackTrace();
        }
        catch (IOException e) {
            request.getRequestDispatcher(jspError).forward(request, response);
            e.printStackTrace();
        }
	    catch (ParserConfigurationException e) {
            request.getRequestDispatcher(jspError).forward(request, response);
            e.printStackTrace();
        }
        catch (XPathExpressionException e) {
            request.getRequestDispatcher(jspError).forward(request, response);
            e.printStackTrace();
        }
        catch (SAXException e) {
            request.getRequestDispatcher(jspError).forward(request, response);
            e.printStackTrace();
        }

    }
}
