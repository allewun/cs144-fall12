package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import javax.servlet.http.HttpUtils;
import java.util.Hashtable;
import java.util.Enumeration;

public class SearchServlet extends HttpServlet implements Servlet {

    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        PrintWriter out = response.getWriter();
        AuctionSearchClient as = new AuctionSearchClient();

        String query = request.getParameter("q");
        int numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
        int numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn"));
        SearchResult[] basicResults = as.basicSearch(query, numResultsToSkip, numResultsToReturn);

        out.println("<html>");
        out.println("<head><title>eBay Keyword Search Results</title></head>");
        out.println("<body>");

        out.println("<h1>Basic Search Query: " + query + "</h1>");
        out.println("<h2>Received " + basicResults.length + " results</h2>");
        out.println("<ol>");
        for (SearchResult result : basicResults) {
            out.println("<li>" + result.getItemId() + ": " + result.getName() + "</li>");
        }
        out.println("</ol>");

        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
