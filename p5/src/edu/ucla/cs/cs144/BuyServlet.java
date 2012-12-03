package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class BuyServlet extends HttpServlet implements Servlet {

    public BuyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String jspDest = "/buyItem.jsp";

        HttpSession session = request.getSession(true);

        String itemId = (String)session.getAttribute("itemId");
        String itemName = (String)session.getAttribute("itemName");
        String itemBuyPrice = (String)session.getAttribute("itemBuyPrice");

        if (session.isNew() || itemBuyPrice == null) {
            jspDest = "/error.jsp";
        } else {
            request.setAttribute("itemId", itemId);
            request.setAttribute("itemName", itemName);
            request.setAttribute("itemBuyPrice", itemBuyPrice);
        }

        request.getRequestDispatcher(jspDest).forward(request, response);
    }
}
