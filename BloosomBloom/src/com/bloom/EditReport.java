package com.bloom;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/editReport")
public class EditReport extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Database configuration details
    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/project";
    final String user = "root";
    final String pass = "amala1021";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String id = request.getParameter("id"); // Report ID

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            // Load JDBC driver
            Class.forName(driver);

            conn = DriverManager.getConnection(url, user, pass);

            // SQL query to fetch report details based on the report ID
            String sql = "SELECT * FROM report WHERE report_id = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, id);

            rs = pst.executeQuery();

            if (rs.next()) {
                // Display the report details in the form for editing
                out.print("<form name='editReport' method='post' action='editReportAction'>");
                out.print("Report ID: <input type='hidden' name='report_id' value='" + rs.getInt("report_id") + "'><br>");
                out.print("Report Type: <input type='text' name='reportType' value='" + rs.getString("report_type") + "' required><br>");
                out.print("Date Generated: <input type='text' name='dateGenerated' value='" + rs.getString("date_generated") + "' required><br>");
                out.print("<input type='submit' value='UPDATE'>");  // Submit button
                out.print("</form>");
            } else {
                out.print("<p>No report found with ID: " + id + "</p>");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            out.print("<p>Error retrieving report details. Please try again later.</p>");
        } finally {
            // Close resources safely
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

