/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;


class MyParser {

    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;

    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };

    static class MyErrorHandler implements ErrorHandler {

        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }

        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }

        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }

    }

    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }

    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }

    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }

    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }

    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }

    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }

        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);

        /* Fill in code here (you will probably need to write auxiliary
            methods). */



        /**************************************************************/
        try {
            /* Create the "create.sql" file */
            File file = new File("load.sql");
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileWriter fstream = new FileWriter(file.getAbsoluteFile());
            BufferedWriter out = new BufferedWriter(fstream);

            /* Time parser */
            SimpleDateFormat format = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");

            /* Go through DOM structure */
            Element root = doc.getDocumentElement();
            Element[] items = getElementsByTagNameNR(root, "Item");

            for (int i = 0; i < items.length; i++) {
                out.write("INSERT INTO Item VALUES(");

                String itemID = items[i].getAttribute("ItemID");
                String name_not_formated = getElementTextByTagNameNR(items[i], "Name");
                String name = "\"" + name_not_formated.replace("\"", "\\\"") + "\"";
                String currently = strip(getElementTextByTagNameNR(items[i], "Currently"));
                String buy_price = getElementTextByTagNameNR(items[i], "Buy_Price");
                if (buy_price == "")
                    buy_price = "NULL";
                else
                    buy_price = strip(buy_price);
                String first_bid = strip(getElementTextByTagNameNR(items[i], "First_Bid"));
                String num_of_bids = getElementTextByTagNameNR(items[i], "Number_of_Bids");

                String started = "";
                String ends = "";
                try {
                    Date started_formated = format.parse(getElementTextByTagNameNR(items[i], "Started"));
                    Date ends_formated = format.parse(getElementTextByTagNameNR(items[i], "Ends"));
                    format.applyPattern("yyyy-MM-dd HH:mm:ss");
                    started = "\'" + format.format(started_formated) + "\'";
                    ends = "\'" + format.format(ends_formated) + "\'";
                    format.applyPattern("MMM-dd-yy HH:mm:ss");
                }
                catch (ParseException pe) {
                    System.out.println("ERROR: Cannot parse dates");
                }
                String sellerID = "\'" +  getElementByTagNameNR(items[i], "Seller").getAttribute("UserID") + "\'";
                String description = getElementTextByTagNameNR(items[i], "Description");
                description = description.replace("\\\"", "\""); /* unescape espaced quotes */
                description = description.replace("\"", "\\\""); /* escape unescaped quotes */
                description = "\"" + description + "\"";


                out.write(
                    itemID + ", " +
                    name + ", " +
                    currently + ", " +
                    buy_price + ", " +
                    first_bid + ", " +
                    num_of_bids + ", " +
                    started + ", " +
                    ends + ", " +
                    sellerID + ", " +
                    description
                );
                out.write(");\n");
            }

            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }

        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        }
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }

        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
