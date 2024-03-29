/*
 * Main.java
 *
 * Created on 6 March 2006, 11:51
 *
 */

package lucene.demo;

import java.util.Iterator;

import lucene.demo.search.*;
import lucene.demo.business.*;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.IndexSearcher;

/**
 *
 * @author John
 */
public class Main {

    /** Creates a new instance of Main */
    public Main() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

      try {
	// build a lucene index
        System.out.println("rebuildIndexes");
        Indexer  indexer = new Indexer();
        indexer.rebuildIndexes();
        System.out.println("rebuildIndexes done");

        // perform search on "Notre Dame museum"
        // and retrieve the result
        System.out.println("performSearch");
        SearchEngine instance = new SearchEngine();
        Hits hits = instance.performSearch("Notre Dame museum");

        System.out.println("Results found: " + hits.length());
        Iterator<Hit> iter =  hits.iterator();
        while(iter.hasNext()){
            Hit hit = iter.next();
            Document doc = hit.getDocument();
            System.out.println(doc.get("name")
                               + " " + doc.get("city")
                               + " (" + hit.getScore() + ")");

        }
        System.out.println("performSearch done");
      } catch (Exception e) {
        System.out.println("Exception caught.\n");
      }
    }

}
