package edu.ucla.cs.cs144;

import java.io.IOException;

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

    public void indexItem(ResultSet item) {
        try {
            IndexWriter writer = getIndexWriter(false);
            Document doc = new Document();

            doc.add(new Field("name", item.getString("name"), Field.Store.YES, Field.Index.TOKENIZED));
            doc.add(new Field("description", item.getString("description"), Field.Store.YES, Field.Index.TOKENIZED));

            writer.addDocument(doc);
        }
	    catch (IOException e) {
            System.out.println("IOException Error");
        }
        catch (SQLException se) {
            System.out.println("SQLException Error");
        }
    }

    public void indexCategory(ResultSet category) {
        try {
            IndexWriter writer = getIndexWriter(false);
            Document doc = new Document();

            doc.add(new Field("category", category.getString("category"), Field.Store.YES, Field.Index.TOKENIZED));

            writer.addDocument(doc);
        }
    	catch (IOException e) {
            System.out.println("IOException Error");
        }
        catch (SQLException se) {
            System.out.println("SQLException Error");
        }
   }

    public void indexBasicKeywords(ResultSet itemAndCategory) {
        try {
        IndexWriter writer = getIndexWriter(false);
        Document doc = new Document();

        String fullSearchableText = itemAndCategory.getString("name") + " " +
                                    itemAndCategory.getString("description") + " " +
                                    itemAndCategory.getString("category");

        doc.add(new Field("basicKeywords", fullSearchableText, Field.Store.YES, Field.Index.TOKENIZED));

        writer.addDocument(doc);
        }
        catch (IOException e) {
            System.out.println("IOException Error");
        }
        catch (SQLException se) {
            System.out.println("SQLException Error");
        }
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

        Statement s = null;
        try {
            s = conn.createStatement();
        }
        catch (SQLException se) {
            System.out.println("SQLException Error");
        }

        // Begin indexing...

        // Open indexWriter
        try {
            getIndexWriter(true);
        }
        catch (IOException e) {
            System.out.println("IOException Error");
        }


        try {
            // Lucene indexing of Items
            ResultSet rs = s.executeQuery("SELECT name, description FROM Items");
            while (rs.next()) {
                indexItem(rs);
            }

            // Lucene indexing of Categories
            rs = s.executeQuery("SELECT category FROM Categories");
            while (rs.next()) {
                indexCategory(rs);
            }

            // Lucene indexing of basic keywords
/*            rs = s.executeQuery("SELECT name, description, category FROM Items i, Categories c WHERE i.itemID = c.itemID");
            while (rs.next()) {
                indexBasicKeywords(rs);
            } */
            

            // Close indexWriter
            closeIndexWriter();

            rs.close();
        }
        catch (SQLException se) {
            System.out.println(se);
        }
        catch (IOException e) {
            System.out.println(e);
        }

        // close the database connection
        try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public static void main(String agrs[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }
}
