package edu.ucla.cs.cs144;

import java.io.IOException;
import java.net.HttpURLConnection;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

public class ProxyServlet extends HttpServlet implements Servlet {

    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String query = "";
        if (request.getParameter("q") != null) {
            query = URLEncoder.encode(request.getParameter("q"), "UTF-8");
        }

        URL googleSuggestService = new URL("http://google.com/complete/search?output=toolbar&q=" + query);
        HttpURLConnection conn = (HttpURLConnection)googleSuggestService.openConnection();

        conn.connect();

        InputStreamReader in = new InputStreamReader((InputStream)conn.getContent());
        BufferedReader buff = new BufferedReader(in);

        StringBuilder serverResponse = new StringBuilder();
        String line;

        while ((line = buff.readLine()) != null) {
            serverResponse.append(line + "\n");
        }

        PrintWriter out = response.getWriter();
        response.setContentType("text/xml");
        out.println(serverResponse.toString());
        out.close();

        // TODO: catch exceptions, error handling
    }
}

