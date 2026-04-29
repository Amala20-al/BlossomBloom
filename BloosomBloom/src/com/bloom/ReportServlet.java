package com.bloom;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/project";
    final String user = "root";
    final String pass = "amala1021";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // Session handling
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("uname") == null) {
            response.sendRedirect("index.html"); // Redirect to login page if no session
            return;
        }
        String name = (String) session.getAttribute("uname");

        // HTML Output
        out.print("<html><body>");
        out.print("<h3>Welcome " + name + "</h3>");
        out.print("<h1>Report</h1>");

        // Form for adding a new report with dropdown for report type
        out.print("<h3>New Report Entry</h3>");
        out.print("<form action='report' method='post'>");
        out.print("Report Type: ");
        out.print("<select name='reportType' required>");
        out.print("<option value='Stock Report'>Stock Report</option>");
        out.print("<option value='Billing Report'>Billing Report</option>");
        out.print("<option value='Order Report'>Order Report</option>");
        out.print("</select><br><br>");
        out.print("Date Generated: <input type='text' name='dateGenerated' required><br><br>");
        out.print("<button type='submit'>SAVE</button>");
        out.print("</form>");

        // Table for displaying report details
        out.print("<h3>Report Details</h3>");
        out.print("<table border='1'>");
        out.print("<tr><th>SLNO</th><th>Report Type</th><th>Date Generated</th><th>EDIT</th><th>DELETE</th></tr>");

        // Database operation to fetch report details
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = conn.prepareStatement("SELECT * FROM report");
             ResultSet rs = pst.executeQuery()) {

            int i = 1; // Serial number for each row
            while (rs.next()) {
                int report_id = rs.getInt("report_id");
                String report_type = rs.getString("report_type");
                String date_generated = rs.getString("date_generated"); // Use correct column name

                out.print("<tr>");
                out.print("<td>" + i + "</td>");
                out.print("<td>" + report_type + "</td>");
                out.print("<td>" + date_generated + "</td>"); // Fixed variable name here
                out.print("<td><a href='editReport?id=" + report_id + "'>Edit</a></td>");
                out.print("<td><a href='deleteReport?id=" + report_id + "'>Delete</a></td>");
                out.print("</tr>");
                i++;
            }
        } catch (SQLException e) {
            out.print("<p>Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }

        out.print("</table>");
        out.print("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get form data
        String reportType = request.getParameter("reportType");
        String dateGenerated = request.getParameter("dateGenerated");

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String insertQuery = "INSERT INTO report (report_type, date_generated) VALUES (?, ?)";
            try (PreparedStatement pst = conn.prepareStatement(insertQuery)) {
                pst.setString(1, reportType);
                pst.setString(2, dateGenerated);  // Correct column name for date_generated

                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    response.sendRedirect("report"); // Redirect to the Report page to see the updated list
                } else {
                    response.sendRedirect("admin"); // Redirect to admin page if insert fails
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("admin"); // Redirect to admin page if an error occurs
        }
    }
}

