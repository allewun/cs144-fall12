package edu.ucla.cs.cs144;

import java.io.IOException;
import java.net.HttpURLConnection;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;

public class ProxyServlet extends HttpServlet implements Servlet {

    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // test data
        PrintWriter out = response.getWriter();
        response.setContentType("text/xml");
        out.println("<?xml version=\"1.0\"?><toplevel><CompleteSuggestion><suggestion data=\"ucla\"/><num_queries int=\"59200000\"/></CompleteSuggestion><CompleteSuggestion><suggestion data=\"ucla football\"/><num_queries int=\"39300000\"/></CompleteSuggestion><CompleteSuggestion><suggestion data=\"ucla football schedule\"/><num_queries int=\"51700000\"/></CompleteSuggestion><CompleteSuggestion><suggestion data=\"ucla extension\"/><num_queries int=\"2660000\"/></CompleteSuggestion><CompleteSuggestion><suggestion data=\"ucla map\"/><num_queries int=\"53900000\"/></CompleteSuggestion><CompleteSuggestion><suggestion data=\"ucla library\"/><num_queries int=\"3170000\"/></CompleteSuggestion><CompleteSuggestion><suggestion data=\"ucla medical center\"/><num_queries int=\"4400000\"/></CompleteSuggestion><CompleteSuggestion><suggestion data=\"ucla jobs\"/><num_queries int=\"51400000\"/></CompleteSuggestion><CompleteSuggestion><suggestion data=\"ucla schedule of classes\"/><num_queries int=\"304000\"/></CompleteSuggestion><CompleteSuggestion><suggestion data=\"ucla store\"/><num_queries int=\"19200000\"/></CompleteSuggestion></toplevel>");
        out.close();
    }
}

