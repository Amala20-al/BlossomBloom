package com.bloom;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/deleteReport")
public class DeleteReport extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Database configuration details
    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/project";
    final String user = "root";
    final String pass = "amala1021";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        String id = request.getParameter("id"); // Get the report ID

        try {
            // Load JDBC driver
            Class.forName(driver);

            try (Connection conn = DriverManager.getConnection(url, user, pass);
                 PreparedStatement pst = conn.prepareStatement("DELETE FROM report WHERE report_id = ?")) {

                pst.setString(1, id);

                // Execute the delete query
                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    response.sendRedirect("report"); // Redirect to the report page after successful deletion
                } else {
                    response.sendRedirect("error.jsp?message=No+report+record+found+with+ID%3A+" + id);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendRedirect("error.jsp?message=Error+deleting+report+record");
        }
    }
}

