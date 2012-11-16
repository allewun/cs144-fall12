package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

public class SearchServlet extends HttpServlet implements Servlet {

    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try {
            AuctionSearchClient as = new AuctionSearchClient();
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/search.jsp");

            String query = request.getParameter("q");
            int numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
            int numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn"));
            SearchResult[] basicResults = as.basicSearch(query, numResultsToSkip, numResultsToReturn);

            String prevLink = "/eBay/search?q=" + query +
                              "&numResultsToSkip=" + (numResultsToSkip - numResultsToReturn) +
                              "&numResultsToReturn=" + numResultsToReturn;
            String nextLink = "/eBay/search?q=" + query +
                              "&numResultsToSkip=" + (numResultsToSkip + numResultsToReturn) +
                              "&numResultsToReturn=" + numResultsToReturn;

            if (basicResults.length < numResultsToReturn) {
                nextLink = null;
            }

            if (numResultsToSkip < numResultsToReturn) {
                prevLink = null;
            }

            request.setAttribute("prevLink", prevLink);
            request.setAttribute("nextLink", nextLink);
            request.setAttribute("numResults", basicResults.length);
            request.setAttribute("basicResults", basicResults);

            rd.forward(request, response);
        }
        catch (ServletException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
