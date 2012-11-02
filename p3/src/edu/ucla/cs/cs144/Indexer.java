package edu.ucla.cs.cs144;

import java.io.IOException;
import java.util.HashMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;

public class Indexer {

    //** FOR DEBUGGING PURPOSES **//
    private static void debug(String msg) {
        System.out.println(msg);
    }

    /** Creates a new instance of Indexer */
    public Indexer() {
    }

    private IndexWriter indexWriter = null;

    public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null) {
            String indexPath = System.getenv("LUCENE_INDEX") + "/index1";
            indexWriter = new IndexWriter(indexPath, new StandardAnalyzer(), create);
        }

        return indexWriter;
    }

    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
    }

    public void indexKeywords (ResultSet itemsRS, String categories) throws IOException, SQLException {
        // categories contains a concatenated string of categories for the current item tuple
        IndexWriter writer = getIndexWriter(false);
        Document doc = new Document();

        doc.add(new Field("itemID", itemsRS.getString("itemID"), Field.Store.YES, Field.Index.NO));
        doc.add(new Field("name", itemsRS.getString("name"), Field.Store.YES, Field.Index.TOKENIZED));
        doc.add(new Field("category", categories, Field.Store.NO, Field.Index.TOKENIZED));
        doc.add(new Field("description", itemsRS.getString("description"), Field.Store.NO, Field.Index.TOKENIZED));

        // Basic default search
        String fullSearchableText = itemsRS.getString("name") + " " +
                                    itemsRS.getString("description") + " " +
                                    categories;
        doc.add(new Field("basicKeywords", fullSearchableText, Field.Store.NO, Field.Index.TOKENIZED));

        writer.addDocument(doc);
    }

    public void rebuildIndexes() {

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
        try {
            conn = DbManager.getConnection(true);
        } catch (SQLException ex) {
            System.out.println(ex);
        }


        /*
         * Add your code here to retrieve Items using the connection
         * and add corresponding entries to your Lucene inverted indexes.
         *
         * You will have to use JDBC API to retrieve MySQL data from Java.
         * Read our tutorial on JDBC if you do not know how to use JDBC.
         *
         * You will also have to use Lucene IndexWriter and Document
         * classes to create an index and populate it with Items data.
         * Read our tutorial on Lucene as well if you don't know how.
         *
         * As part of this development, you may want to add
         * new methods and create additional Java classes.
         * If you create new classes, make sure that
         * the classes become part of "edu.ucla.cs.cs144" package
         * and place your class source files at src/edu/ucla/cs/cs144/.
         *
         */

        try {
            Statement s1 = conn.createStatement(); // Items table
            Statement s2 = conn.createStatement(); // Categories table

            // Begin indexing...
            debug("Starting to index...");

            // Open indexWriter
            getIndexWriter(true);

            debug("Lucene indexing of keywords...");

            // SQL SELECT statements to obtain the tuples
            ResultSet itemsRS = s1.executeQuery("SELECT itemID, name, description FROM Items ORDER BY itemID");
            ResultSet categoriesRS = s2.executeQuery("SELECT * FROM Categories ORDER BY itemID");

            // Concatenate all of the categories into one string for easy lucene indexing
            HashMap<Integer, String> combinedCategories = new HashMap<Integer, String>();
            while (categoriesRS.next()) {
                int id = categoriesRS.getInt("itemID");
                String currentCategory = categoriesRS.getString("category");
               
                // If there is already an id key in the Hashmap, append the current category 
                if (combinedCategories.containsKey(id)) {
                    combinedCategories.put(id, combinedCategories.get(id) + " " +
                        currentCategory);
                }
                // If the id is not in the HashMap, add the current id and category into the HashMap
                else {
                    combinedCategories.put(id, currentCategory);
                }
                        
            }

            // Index the items with their corresponding categories
            while (itemsRS.next()) {
                indexKeywords(itemsRS, combinedCategories.get(itemsRS.getInt("itemId")));
            }


            // Close indexWriter
            closeIndexWriter();
            
            itemsRS.close();
            categoriesRS.close();
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
        catch (IOException ex) {
            System.out.println(ex);
        }


        // close the database connection
        try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }
}
