package edu.ucla.cs.cs144;

import java.util.Calendar;
import java.util.Date;

import edu.ucla.cs.cs144.AuctionSearch;
import edu.ucla.cs.cs144.SearchResult;
import edu.ucla.cs.cs144.SearchConstraint;
import edu.ucla.cs.cs144.FieldName;

public class AuctionSearchTest {
	public static void main(String[] args1)
	{
		AuctionSearch as = new AuctionSearch();

		String message = "Test message";
		String reply = as.echo(message);
		System.out.println("Reply: " + reply);
		
		String query = "superman";
		SearchResult[] basicResults = as.basicSearch(query, 0, 20);
		System.out.println("Basic Seacrh Query: " + query);
		System.out.println("Received " + basicResults.length + " results");
		for(SearchResult result : basicResults) {
			System.out.println(result.getItemId() + ": " + result.getName());
		}
		
		SearchConstraint constraint =
		    new SearchConstraint(FieldName.BuyPrice, "5.99"); 
		SearchConstraint[] constraints = {constraint};
		SearchResult[] advancedResults = as.advancedSearch(constraints, 0, 20);
		System.out.println("Advanced Seacrh");
		System.out.println("Received " + advancedResults.length + " results");
		for(SearchResult result : advancedResults) {
			System.out.println(result.getItemId() + ": " + result.getName());
		}
		
		String itemId = "1497595357";
		String item = as.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);

		// Add your own test here
		System.out.println("==Testing getXMLDataForItemId==");

		// Basic
		itemId = "1045524027";
		item = as.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);

		// No bids or description
		itemId = "1497595357";
		item = as.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);

		// Javascript code in the description
		itemId = "1497597033";
		item = as.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);

		// & sign in the name
		itemId = "1045710634";
		item = as.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);

		// No matching item Id
		itemId = "10";
		item = as.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);

		// Blank item Id
		itemId = "";
		item = as.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);
		
		// Random characters in item Id
		itemId = "&)_!^k,j,";
		item = as.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);

		// Buy_Price
		itemId = "1043608482";
		item = as.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);
	}
}
