package edu.ucla.cs.cs144;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.io.IOException;

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

    public AuctionSearch(){
        try {
            searcher = new IndexSearcher(System.getenv("LUCENE_INDEX") + "/project3_index");
            parser = new QueryParser("name", new StandardAnalyzer());
        }
        catch (IOException e) {
            System.out.println(e);
        }
        finally {
            System.out.println("uh oh");
        }
    }

    private IndexSearcher searcher = null;
    private QueryParser parser = null;
    private String message = "testing";

    public Hits performSearch(String queryString) throws IOException, ParseException {
        Query query = parser.parse(queryString);
        Hits hits = searcher.search(query);

        return hits;
    }

    public SearchResult[] basicSearch(String query, int numResultsToSkip, int numResultsToReturn) {
        SearchResult[] searchResults = null;

        try {
            Hits hits = performSearch(query);

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

    public SearchResult[] advancedSearch(SearchConstraint[] constraints,
            int numResultsToSkip, int numResultsToReturn) {
        // TODO: Your code here!
        return new SearchResult[0];
    }

    public String getXMLDataForItemId(String itemId) {
        // TODO: Your code here!
        return null;
    }

    public String echo(String message) {
        return message;
    }

}
