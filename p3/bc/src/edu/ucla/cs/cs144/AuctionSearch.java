package edu.ucla.cs.cs144;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.io.IOException;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import java.util.Date;
import java.util.Iterator;
import java.text.SimpleDateFormat;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchConstraint;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {

    /*
     * You will probably have to use JDBC to access MySQL data
     * Lucene IndexSearcher class to lookup Lucene index.
     * Read the corresponding tutorial to learn about how to use these.
     *
     * Your code will need to reference the directory which contains your
     * Lucene index files.  Make sure to read the environment variable
     * $LUCENE_INDEX with System.getenv() to build the appropriate path.
     *
     * You may create helper functions or classes to simplify writing these
     * methods. Make sure that your helper functions are not public,
     * so that they are not exposed to outside of this class.
     *
     * Any new classes that you create should be part of
     * edu.ucla.cs.cs144 package and their source files should be
     * placed at src/edu/ucla/cs/cs144.
     *
     */

    private IndexSearcher searcher = null;
    private QueryParser parser = null;
    private String message = "";

    public AuctionSearch() {
        try {
            searcher = new IndexSearcher(System.getenv("LUCENE_INDEX") + "/project3_index");
        }
        catch (IOException e) {}
    }

    public Hits performSearch(String field, String queryString) throws IOException, ParseException {
        parser = new QueryParser(field, new StandardAnalyzer());

        Query query = parser.parse(queryString);
        Hits hits = searcher.search(query);

        return hits;
    }

    public SearchResult[] basicSearch(String query, int numResultsToSkip, int numResultsToReturn) {
        SearchResult[] searchResults = null;

        try {
            Hits hits = performSearch("basicKeywords", query);

            int indexStart = numResultsToSkip;
            int indexEnd   = Math.min(hits.length(), numResultsToReturn);

            searchResults = new SearchResult[indexEnd - indexStart];

            for (int i = indexStart; i < indexEnd; ++i) {
                Document doc = hits.doc(i);
                String itemID = doc.get("itemID");
                String name = doc.get("name");

                searchResults[i] = new SearchResult(itemID, name);
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
        catch (ParseException e) {
            System.out.println(e);
        }

        return searchResults;
    }

    public SearchResult[] advancedSearch(SearchConstraint[] constraints, int numResultsToSkip, int numResultsToReturn) {
        SearchResult[] searchResults = null;

        try {
            Connection conn = DbManager.getConnection(true);
            Statement s = conn.createStatement();

            int numConstraints = constraints.length;

            Map<String,String> mysqlHash = new HashMap<String,String>();
            mysqlHash.put(FieldName.SellerId, "SELECT itemID FROM Items WHERE sellerID = ");
            mysqlHash.put(FieldName.BuyPrice, "SELECT itemID FROM Items WHERE buy_price = ");
            mysqlHash.put(FieldName.BidderId, "SELECT itemID FROM Users WHERE userID = ");
            mysqlHash.put(FieldName.EndTime,  "SELECT itemID FROM Users WHERE ends = ");


            // hits for each of the constraints
            Hits[] hitsArray = new Hits[numConstraints];

            // results sets for each for the constraints
            Set[] setsArray = new Set[numConstraints];

            // for each constraint
            for (int i = 0; i < numConstraints; ++i) {
                SearchConstraint constraint = constraints[i];
                String constraintType = constraint.getFieldName();
                String constraintValue = constraint.getValue();
                Set<String> constraintSet = new HashSet<String>();

                // fields that require MySQL
                if (mysqlHash.get(constraint.getFieldName()) != null) {

                    try {
                        String mysqlQuery = mysqlHash.get(constraintType) + constraintValue;
                        ResultSet rs = s.executeQuery(mysqlQuery);

                        while (rs.next()) {
                            String id = rs.getString("itemID");
                            constraintSet.add(id);
                        }
                    }
                    catch (SQLException e) {}
                }

                // fields that use Lucene index
                else {
                    try {
                        Hits constraintHits = performSearch(constraintType, constraintValue);
                        for (int j = 0; j < constraintHits.length(); ++i) {
                            Document doc = constraintHits.doc(j);

                            constraintSet.add(doc.get("itemID"));
                        }
                    }
                    catch (IOException e) {}
                    catch (ParseException e) {}
                }

                setsArray[i] = constraintSet;
            }

            // retain the itemIDs in the intersection of all the sets
            for (int i = 1; i < numConstraints; ++i) {
                setsArray[0].retainAll(setsArray[i]);
            }

            // construct the final resulting array
            Set<String> resultSet = setsArray[0];
            searchResults = new SearchResult[resultSet.size()];
            int i = 0;

            for (String itemId : resultSet) {
                String retrieveNameFromId = "SELECT name FROM Items WHERE itemID = " + itemId;
                ResultSet rs = s.executeQuery(retrieveNameFromId);

                if (rs.next()) {
                    searchResults[i] = new SearchResult(itemId, rs.getString("name"));
                }

                i++;
            }

            conn.close();
        }
        catch (SQLException ex) {
            System.out.println("SQLException caught");
            System.out.println("---");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
                System.out.println("SQLState  : " + ex.getSQLState());
                System.out.println("ErrorCode : " + ex.getErrorCode());
                System.out.println("---");
                ex = ex.getNextException();
            }
        }

        return searchResults;
    }

    public String getXMLDataForItemId(String itemId) {
        /*
<Item ItemID="">
    <Name></Name>
    <Category></Category> (+)
    <Currently></Currently>
    <Buy_Price></Buy_Price> (?)
    <First_Bid></First_Bid>
    <Number_of_Bids></Number_of_Bids>
    <Bids>
        <Bid> (*)
            <Bidder UserID="" Rating="">
                <Location></Location> (?)
                <Country></Country> (?)
            </Bidder>
            <Time></Time>
            <Amount></Amount>
        </Bid>
    </Bids>
    <Location></Location>
    <Country></Country>
    <Started></Started>
    <Ends></Ends>
    <Seller UserID="" Rating="" />
    <Description></Description>
</Item>
        */

        StringBuilder xmlResult = new StringBuilder();

        try {
            Connection conn = DbManager.getConnection(true);
            Statement s1 = conn.createStatement();
            Statement s2 = conn.createStatement();
            Statement s3 = conn.createStatement();
            Statement s4 = conn.createStatement();

            xmlResult.append("<Item ItemID=\"" + itemId + "\">\n"); // <Item ItemID="">

            // Execute SQL queries for the item
            ResultSet itemsRS = s1.executeQuery("SELECT * FROM Items WHERE ItemID = " + itemId);
            ResultSet bidsRS = s2.executeQuery("SELECT * FROM Bids WHERE ItemID = " + itemId);
            ResultSet categoriesRS = s3.executeQuery("SELECT * FROM Categories WHERE ItemID = " + itemId);
            // Get the location and country for all of the bidders
            ArrayList<String> bidders = new ArrayList<String>();
            while (bidsRS.next()) {
                bidders.add(bidsRS.getString("userID"));
            }
            boolean bidsExist = false;
            ResultSet usersRS = null;
            if (bidders.size() > 0) {
                String usersQuery = "SELECT * FROM Users WHERE userID = " + bidders.get(0);
                for (int i = 1; i < bidders.size(); i++) {
                    usersQuery = usersQuery + "OR userID = " + bidders.get(i);
                }
                usersRS = s4.executeQuery(usersQuery);
            }

            itemsRS.next();
            String name = itemsRS.getString("name");
            xmlResult.append("  <Name>" + name + "</Name>\n"); // <Name></Name>

            while (categoriesRS.next()) {
                String currentCategory = categoriesRS.getString("category");
                xmlResult.append("  <Category>" + currentCategory + "</Category>\n"); // <Category></Category>
            }

            String currently = itemsRS.getString("currently");
            xmlResult.append("  <Currently>" + currently + "</Currently>\n"); // <Currently></Currently>

            String buy_price = itemsRS.getString("buy_price");
            if (buy_price != null) {
                xmlResult.append("  <Buy_Price>" + buy_price + "</Buy_Price>\n"); // <Buy_Price></Buy_Price>
            }
            String first_bid = itemsRS.getString("first_bid");
            xmlResult.append("  <First_Bid>" + first_bid + "</First_Bid>\n"); // <First_Bid></First_Bid>

            String num_of_bids = itemsRS.getString("num_of_bids");
            xmlResult.append("  <Number_of_Bids>" + num_of_bids + "</Number_of_Bids>\n"); // <Number_of_Bids></Number_of_Bids>

            if (bidsExist) {
                while (usersRS.next()) {
                    xmlResult.append("  <Bids>\n"); // <Bids>
                    xmlResult.append("    <Bid>\n"); // <Bid>
                    String userID = usersRS.getString("userID");
                    String rating = usersRS.getString("rating");
                    String location = usersRS.getString("location");
                    String country = usersRS.getString("country");
                    xmlResult.append("      <Bidder UserID=\"" + userID + "\" Rating=\"" + rating + "\">\n"); // <Bidder UserID="" Rating="">
                    xmlResult.append("        <Location>" + location + "</Location>\n"); // <Location></Location>
                    xmlResult.append("        <Country>" + country + "</Country>\n"); // <Country></Country>
                    xmlResult.append("      </Bidder>\n"); // </Bidder>
                    
                    Statement currentBidStatement = conn.createStatement();
                    // Time
                    // Amount
                }
            }
            xmlResult.append("  </Bids>"); // </Bids>
        }
        catch (SQLException ex) {
            System.out.println("SQLException caught");
            System.out.println("---");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
                System.out.println("SQLState  : " + ex.getSQLState());
                System.out.println("ErrorCode : " + ex.getErrorCode());
                System.out.println("---");
                ex = ex.getNextException();
            }
        }

        return xmlResult.toString();
    }

    public String echo(String message) {
        return message;
    }

}
