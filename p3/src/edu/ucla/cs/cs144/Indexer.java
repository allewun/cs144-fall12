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

    /** Creates a new instance of Indexer */
    public Indexer() {}

    private IndexWriter indexWriter = null;

    public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null) {
            String indexPath = System.getenv("LUCENE_INDEX") + "/project3_index";
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
        IndexWriter writer = getIndexWriter(false);
        Document doc = new Document();

        // Advanced keyword search (individual fields)
        String itemID          = itemsRS.getString("itemID");
        String itemName        = itemsRS.getString("name");
        String itemDescription = itemsRS.getString("description");

         // Basic keyword search text (union of item name, description, and its categories)
        String fullSearchableText = itemName + " " +
                                    itemDescription + " " +
                                    categories;

        doc.add(new Field("itemID", itemID, Field.Store.YES, Field.Index.NO));
        doc.add(new Field("name", itemName, Field.Store.YES, Field.Index.TOKENIZED));
        doc.add(new Field("category", categories, Field.Store.NO, Field.Index.TOKENIZED));
        doc.add(new Field("description", itemDescription, Field.Store.NO, Field.Index.TOKENIZED));
        doc.add(new Field("basicKeywords", fullSearchableText, Field.Store.NO, Field.Index.TOKENIZED));

        writer.addDocument(doc);
    }

    public void rebuildIndexes() {

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
        try {
            conn = DbManager.getConnection(true);
        }
        catch (SQLException ex) {
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
            Statement s_items = conn.createStatement();      // Items table
            Statement s_categories = conn.createStatement(); // Categories table

            // Open indexWriter
            getIndexWriter(true);

            // SQL SELECT statements to obtain the tuples
            ResultSet itemsRS = s_items.executeQuery("SELECT itemID, name, description FROM Items");
            ResultSet categoriesRS = s_categories.executeQuery("SELECT * FROM Categories");

            // Concatenate all of the categories of a particular item into a single string
            // (to eliminate duplicate itemID tuples)
            HashMap<Integer, String> allCategories = new HashMap<Integer,String>();
            while (categoriesRS.next()) {
                int id = categoriesRS.getInt("itemID");
                String currentCategory = categoriesRS.getString("category");

                // Add the {itemID => category} key-value pair into the HashMap
                // If there is a key-collision, append the new category onto the
                // existing category string value
                if (!allCategories.containsKey(id)) {
                    allCategories.put(id, currentCategory);
                }
                else {
                    allCategories.put(id, allCategories.get(id) + " " + currentCategory);
                }
            }

            // Index the items with their corresponding categories
            while (itemsRS.next()) {
                indexKeywords(itemsRS, allCategories.get(itemsRS.getInt("itemID")));
            }

            itemsRS.close();
            categoriesRS.close();

            // Close indexWriter
            closeIndexWriter();
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
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }
}
