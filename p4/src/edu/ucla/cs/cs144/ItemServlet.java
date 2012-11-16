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

public class ItemServlet extends HttpServlet implements Servlet {

    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/item.jsp");

            AuctionSearchClient as = new AuctionSearchClient();

            String itemId = request.getParameter("id");
            String itemXML = as.getXMLDataForItemId(itemId);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(itemXML)));
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = xpath.compile("Item/Name");

            String itemName = expr.evaluate(doc);

            expr = xpath.compile("Item/Description");
            String itemDescription = expr.evaluate(doc);

            request.setAttribute("itemName", itemName);
            request.setAttribute("itemDescription", itemDescription);
            rd.forward(request, response);
            //request.getRequestDispatcher("/item.jsp").forward(request, response);
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
