package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import java.net.URLEncoder;
import java.util.regex.Pattern;

public class SearchServlet extends HttpServlet implements Servlet {

    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String jspError = "/error.jsp";

        try {
            AuctionSearchClient as = new AuctionSearchClient();
            String jspDest = "/search.jsp";

            boolean isError = false;

            // Parameter values
            String query = "";
            int numResultsToSkip = 0;
            int numResultsToReturn = 10;
            Pattern nonNumbers = Pattern.compile("\\D");

            if (request.getParameter("numResultsToSkip") == null ||
                request.getParameter("numResultsToReturn") == null ||
                request.getParameter("numResultsToSkip").equals("") ||
                request.getParameter("numResultsToReturn").equals("") ||
                nonNumbers.matcher(request.getParameter("numResultsToSkip")).find() ||
                nonNumbers.matcher(request.getParameter("numResultsToReturn")).find())
            {
                isError = true;
            }

            if (request.getParameter("q") != null) {
                query = URLEncoder.encode(request.getParameter("q"), "UTF-8");
            }
            if (request.getParameter("numResultsToSkip") != null && !isError) {
                numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
            }
            if (request.getParameter("numResultsToReturn") != null && !isError) {
                numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn"));
            }

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
            if (isError ||
                numResultsToSkip < 0 ||
                numResultsToReturn < 0 ||
                (basicResults.length > 0 && basicResults[0].getItemId().equals("-1")))
            {
                jspDest = jspError;
            }

            request.getRequestDispatcher(jspDest).forward(request, response);
        }
        catch (ServletException e) {
            request.getRequestDispatcher(jspError).forward(request, response);
            e.printStackTrace();
        }
        catch (IOException e) {
            request.getRequestDispatcher(jspError).forward(request, response);
            e.printStackTrace();
        }
    }
}
