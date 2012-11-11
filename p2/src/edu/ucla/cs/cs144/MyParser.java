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

    /* Surrounds some text with double quotes.
     */
    static String quote(String text) {
        return "\"" + text + "\"";
    }

    /* Escapes characters in text that would break MySQL loading and
     * surrounds it with double quotes.
     */
    static String escapeAndQuote(String text) {
        text = text.replace("\\", "\\\\"); // escape backslashes
        text = text.replace("\"", "\\\""); // escape quotes

        return quote(text);
    }

    /* Formats a date from the format used in the .XML files to one
     * that MySQL can use.
     */
    static String formatDate(String text) {
        /* Time parser */
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");

        // Parse the date
        try {
            Date formatted = dateFormat.parse(text);
            dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");

            text = quote(dateFormat.format(formatted));

            dateFormat.applyPattern("MMM-dd-yy HH:mm:ss");
        }
        catch (ParseException pe) {
            System.out.println("ERROR: Cannot parse dates");
        }

        return text;
    }

    /* Returns the string "\N" if the text is empty,
     * otherwise return the some default case.
     */
    static String nullify(String text, String fallbackText) {
        return (text.equals("")) ? "\\N" : fallbackText;
    }

    static String nullify(String text) {
        return nullify(text, text);
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

        try {

            // The storage for the parsed data to be used for table creation
            Vector<String[]> parsedItems      = new Vector<String[]>();
            Vector<String[]> parsedUsers      = new Vector<String[]>();
            Vector<String[]> parsedBids       = new Vector<String[]>();
            Vector<String[]> parsedCategories = new Vector<String[]>();

            //==================================================
            // Step 1:
            //   Parse the data and store in the above vectors
            //==================================================

            // Go through DOM structure
            Element root = doc.getDocumentElement();
            Element[] items = getElementsByTagNameNR(root, "Item");

            // For each <Item>
            for (int i = 0; i < items.length; ++i) {

                Element curItem = items[i];
                Element seller = getElementByTagNameNR(curItem, "Seller");

                String itemID       = curItem.getAttribute("ItemID");
                String name         = getElementTextByTagNameNR(curItem, "Name");
                String currently    = getElementTextByTagNameNR(curItem, "Currently");
                String buyPrice     = getElementTextByTagNameNR(curItem, "Buy_Price");
                String firstBid     = getElementTextByTagNameNR(curItem, "First_Bid");
                String numberOfBids = getElementTextByTagNameNR(curItem, "Number_of_Bids");
                String started      = getElementTextByTagNameNR(curItem, "Started");
                String ends         = getElementTextByTagNameNR(curItem, "Ends");
                String sellerID     = seller.getAttribute("UserID");
                String description  = getElementTextByTagNameNR(curItem, "Description");

                String sellerLocation = getElementTextByTagNameNR(curItem, "Location");
                String sellerCountry  = getElementTextByTagNameNR(curItem, "Country");
                String sellerRating   = seller.getAttribute("Rating");

                // add the User (seller)
                String[] sellerRecord = {sellerID, sellerRating, sellerLocation, sellerCountry};
                parsedUsers.add(sellerRecord);

                // add the Item
                String[] itemRecord = {itemID, name, currently, buyPrice, firstBid, numberOfBids, started, ends, sellerID, description};
                parsedItems.add(itemRecord);

                // For each <Bid>
                Element bidsElement = getElementByTagNameNR(curItem, "Bids");
                Element[] bids = getElementsByTagNameNR(bidsElement, "Bid");
                for (int j = 0; j < bids.length; ++j) {

                    Element curBid = bids[j];
                    Element bidder = getElementByTagNameNR(curBid, "Bidder");

                    String userID    = bidder.getAttribute("UserID");
                    String bidTime   = getElementTextByTagNameNR(curBid, "Time");
                    String bidAmount = getElementTextByTagNameNR(curBid, "Amount");

                    String bidderRating   = bidder.getAttribute("Rating");
                    String bidderLocation = getElementTextByTagNameNR(bidder, "Location");
                    String bidderCountry  = getElementTextByTagNameNR(bidder, "Country");

                    // add the User (bidder)
                    String[] bidderRecord = {userID, bidderRating, bidderLocation, bidderCountry};
                    parsedUsers.add(bidderRecord);

                    // add the Bid
                    String[] bidRecord = {itemID, userID, bidTime, bidAmount};
                    parsedBids.add(bidRecord);
                }

                // For each <Category>
                Element[] categories = getElementsByTagNameNR(curItem, "Category");
                for (int j = 0; j < categories.length; ++j) {

                    String category = categories[j].getTextContent();

                    // add the Category
                    String[] categoryRecord = {itemID, category};
                    parsedCategories.add(categoryRecord);
                }
            }


            //==================================================
            // Step 2:
            //   Using parsed data stored in the vectors,
            //   create the load.sql file
            //==================================================

            StringBuilder itemsSQL = new StringBuilder();
            StringBuilder usersSQL = new StringBuilder();
            StringBuilder bidsSQL =  new StringBuilder();
            StringBuilder categoriesSQL = new StringBuilder();

            // Generate the INSERT statements for the Items table
            for (int i = 0; i < parsedItems.size(); ++i) {

                // 0: itemID      INT PRIMARY KEY
                // 1: name        VARCHAR(100) NOT NULL
                // 2: currently   DECIMAL(8,2) NOT NULL
                // 3: buy_price   DECIMAL(8,2)
                // 4: first_bid   DECIMAL(8,2) NOT NULL
                // 5: num_of_bids INT NOT NULL
                // 6: started     TIMESTAMP NOT NULL
                // 7: ends        TIMESTAMP NOT NULL
                // 8: sellerID    VARCHAR(40) NOT NULL
                // 9: description VARCHAR(4000)

                String[] item = parsedItems.get(i);

                itemsSQL.append(item[0] + ",");
                itemsSQL.append(escapeAndQuote(item[1]) + ",");
                itemsSQL.append(strip(item[2]) + ",");
                itemsSQL.append(nullify(strip(item[3])) + ",");
                itemsSQL.append(strip(item[4]) + ",");
                itemsSQL.append(item[5] + ",");
                itemsSQL.append(formatDate(item[6]) + ",");
                itemsSQL.append(formatDate(item[7]) + ",");
                itemsSQL.append(escapeAndQuote(item[8]) + ",");
                itemsSQL.append(nullify(item[9], escapeAndQuote(item[9])) + "\n");
            }

            // Generate the INSERT statements for the Users table
            for (int i = 0; i < parsedUsers.size(); ++i) {
                // 0: userID   VARCHAR(40) PRIMARY KEY
                // 1: rating   INT NOT NULL
                // 2: location VARCHAR(40)
                // 3: country  VARCHAR(40)

                String[] user = parsedUsers.get(i);

                usersSQL.append(escapeAndQuote(user[0]) + ",");
                usersSQL.append(user[1] + ",");
                usersSQL.append(nullify(user[2], escapeAndQuote(user[2])) + ",");
                usersSQL.append(nullify(user[3], escapeAndQuote(user[3])) + "\n");
            }

            // Generate the INSERT statements for the Bids table
            for (int i = 0; i < parsedBids.size(); ++i) {
                // 0: itemID VARCHAR(40) NOT NULL
                // 1: userID VARCHAR(40) NOT NULL
                // 2: time   TIMESTAMP NOT NULL
                // 3: amount DECIMAL(8,2) NOT NULL
                // PRIMARY KEY (itemID, userID, time)

                String[] bid = parsedBids.get(i);

                bidsSQL.append(bid[0] + ",");
                bidsSQL.append(escapeAndQuote(bid[1]) + ",");
                bidsSQL.append(formatDate(bid[2]) + ",");
                bidsSQL.append(strip(bid[3]) + "\n");
            }

            // Generate the INSERT statements for the Category table
            for (int i = 0; i < parsedCategories.size(); ++i) {
                // 0: itemID   INT NOT NULL
                // 1: category VARCHAR(40) NOT NULL
                // PRIMARY KEY (itemID, category)

                String[] category = parsedCategories.get(i);

                categoriesSQL.append(category[0] + ",");
                categoriesSQL.append(escapeAndQuote(category[1]) + "\n");
            }

            // Create the .dat files
            File items_data_file      = new File("Items.dat");
            File users_data_file      = new File("Users.dat");
            File bids_data_file       = new File("Bids.dat");
            File categories_data_file = new File("Categories.dat");

            FileWriter fstream_items      = new FileWriter(items_data_file, true); // 2nd arg toggles append mode
            FileWriter fstream_users      = new FileWriter(users_data_file, true);
            FileWriter fstream_bids       = new FileWriter(bids_data_file, true);
            FileWriter fstream_categories = new FileWriter(categories_data_file, true);

            BufferedWriter out_items      = new BufferedWriter(fstream_items);
            BufferedWriter out_users      = new BufferedWriter(fstream_users);
            BufferedWriter out_bids       = new BufferedWriter(fstream_bids);
            BufferedWriter out_categories = new BufferedWriter(fstream_categories);

            out_items.write(itemsSQL.toString());
            out_users.write(usersSQL.toString());
            out_bids.write(bidsSQL.toString());
            out_categories.write(categoriesSQL.toString());

            out_items.close();
            out_users.close();
            out_bids.close();
            out_categories.close();
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
