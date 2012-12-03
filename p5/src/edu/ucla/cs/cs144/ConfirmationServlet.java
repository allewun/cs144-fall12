package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.text.SimpleDateFormat;


public class ConfirmationServlet extends HttpServlet implements Servlet {

    public ConfirmationServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String jspDest = "/confirmation.jsp";

        HttpSession session = request.getSession(true);

        String creditCardNumber = request.getParameter("creditCardNumber");
        String itemId = (String)session.getAttribute("itemId");
        String itemName = (String)session.getAttribute("itemName");
        String itemBuyPrice = (String)session.getAttribute("itemBuyPrice");

        if (session.isNew() || itemBuyPrice == null || creditCardNumber == null || !creditCardNumber.matches("[0-9]+")) {
            jspDest = "/error.jsp";
        } else {
            request.setAttribute("itemId", itemId);
            request.setAttribute("itemName", itemName);
            request.setAttribute("itemBuyPrice", itemBuyPrice);
            request.setAttribute("creditCardNumber", creditCardNumber);

            Date d = new Date();
            String currentDate = (new SimpleDateFormat("MM/dd/yyyy")).format(d);
            String currentTime = (new SimpleDateFormat("HH:mm:ss.SSS")).format(d);
            request.setAttribute("currentDate", currentDate);
            request.setAttribute("currentTime", currentTime);
        }

        request.getRequestDispatcher(jspDest).forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
