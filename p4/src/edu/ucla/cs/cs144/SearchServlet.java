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
            String jspDest = "/search.jsp";

            // Parameter values
            String query = request.getParameter("q");
            int numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
            int numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn"));

            // Search and store the results
            SearchResult[] basicResults = as.basicSearch(query, numResultsToSkip, numResultsToReturn);

            // Previous and Next links construction
            String urlBase = "/eBay/search?q=" + query;
            int prevSkip = numResultsToSkip - numResultsToReturn;
            int nextSkip = numResultsToSkip + numResultsToReturn;

            if (numResultsToSkip < numResultsToReturn) {
                prevSkip = 0;
            }

            String prevLink = urlBase + "&numResultsToSkip=" + prevSkip + "&numResultsToReturn=" + numResultsToReturn;
            String nextLink = urlBase + "&numResultsToSkip=" + nextSkip + "&numResultsToReturn=" + numResultsToReturn;

            if (numResultsToSkip == 0) {
                prevLink = null;
            }

            if (basicResults.length < numResultsToReturn) {
                nextLink = null;
            }

            // Send relevant variables to the JSP page
            request.setAttribute("prevLink", prevLink);
            request.setAttribute("nextLink", nextLink);
            request.setAttribute("numResults", basicResults.length);
            request.setAttribute("basicResults", basicResults);

            // Error handling
            if (numResultsToSkip < 0 || numResultsToReturn < 0) {
                jspDest = "/error.jsp";
            }

            RequestDispatcher rd = getServletContext().getRequestDispatcher(jspDest);
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
