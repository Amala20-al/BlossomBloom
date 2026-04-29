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

@WebServlet("/editCustomerAction")
public class EditCustomerAction extends HttpServlet {

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
        
        // Retrieve the vendor details from the form submission
        int cust_id = Integer.parseInt(request.getParameter("cust_id"));
        String cust_name = request.getParameter("customerName");
        Long contact_info = Long.parseLong(request.getParameter("contactInfo"));
        String address = request.getParameter("address");
        
        
        // Debugging output to console
        System.out.println("Updating vendor with ID: " + cust_id);
        System.out.println("New Vendor Name: " + cust_name);
        System.out.println("New Contact Info: " + contact_info);
        System.out.println("New Address: " + address);
        
        
        try {
            // Load the JDBC driver
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Prepare the SQL statement to update the vendor details
            pst = conn.prepareStatement("UPDATE customer SET cust_name=?, contact_info=?, address=? WHERE cust_id=?");
            pst.setString(1, cust_name);
            pst.setLong(2, contact_info);
            pst.setString(3, address);
        
            pst.setInt(4, cust_id); // Set the vendor ID to locate the record
            
            // Execute the update query
            int updatedRows = pst.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("Customer updated successfully.");
                response.sendRedirect("customer"); // Redirect to the vendor management page after updating
            } else {
                System.out.println("No customer found with ID: " + cust_id);
                response.sendRedirect("customer"); // Redirect to an error page if the update fails
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendRedirect("customer"); // Redirect to an error page in case of exception
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


