package com.bloom;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/deleteBilling")
public class DeleteBilling extends HttpServlet {

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

        String id = request.getParameter("id"); // Get the billing ID

        Connection conn = null;
        PreparedStatement pst = null;

        try {
            // Load JDBC driver
            Class.forName(driver);

            conn = DriverManager.getConnection(url, user, pass);

            // SQL query to delete the billing record by billing_id
            String sql = "DELETE FROM billing WHERE billing_id = ?"; // Use billing_id for the billing table
            pst = conn.prepareStatement(sql);
            pst.setString(1, id);

            // Execute the delete query
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                out.print("Billing record with ID " + id + " deleted successfully.");
            } else {
                out.print("No billing record found with ID: " + id);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            out.print("Error deleting billing record.");
        } finally {
            // Close resources safely
            try {
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
