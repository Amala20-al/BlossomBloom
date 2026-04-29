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

@WebServlet("/editBillingAction")
public class EditBillingAction extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    Connection conn = null;
    PreparedStatement pst = null;

    // Database configuration details
    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/project";
    final String user = "root";
    final String pass = "amala1021";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Retrieve the billing details from the form submission
        int billing_id = Integer.parseInt(request.getParameter("billing_id"));
        String cust_name = request.getParameter("customerName");
        long total_amount = Long.parseLong(request.getParameter("totalAmount"));
        String billing_date = request.getParameter("billingDate");
        
        // Debugging output to console
        System.out.println("Updating billing with ID: " + billing_id);
        System.out.println("Customer Name: " + cust_name);
        System.out.println("Total Amount: " + total_amount);
        System.out.println("Billing Date: " + billing_date);
        
        try {
            // Load the JDBC driver
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Prepare the SQL statement to update the billing details
            pst = conn.prepareStatement("UPDATE billing SET cust_name=?, total_amount=?, billing_date=? WHERE billing_id=?");
            pst.setString(1, cust_name);
            pst.setLong(2, total_amount);
            pst.setString(3, billing_date);
            pst.setInt(4, billing_id); // Set the billing ID to locate the record
            
            // Execute the update query
            int updatedRows = pst.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("Billing updated successfully.");
                response.sendRedirect("billing"); // Redirect to the billing page after updating
            } else {
                System.out.println("No billing record found with ID: " + billing_id);
                response.sendRedirect("billing"); // Redirect to an error page if the update fails
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendRedirect("error.jsp"); // Redirect to an error page in case of exception
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
