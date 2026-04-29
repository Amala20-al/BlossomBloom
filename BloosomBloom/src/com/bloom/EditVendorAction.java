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

@WebServlet("/editVendorAction")
public class EditVendorAction extends HttpServlet {

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
        int ven_id = Integer.parseInt(request.getParameter("ven_id"));
        String vendor_name = request.getParameter("vendorName");
        Long contact_info = Long.parseLong(request.getParameter("contactInfo"));
        String address = request.getParameter("address");
        String items_supplied = request.getParameter("itemSupply");
        
        // Debugging output to console
        System.out.println("Updating vendor with ID: " + ven_id);
        System.out.println("New Vendor Name: " + vendor_name);
        System.out.println("New Contact Info: " + contact_info);
        System.out.println("New Address: " + address);
        System.out.println("New Items Supplied: " + items_supplied);
        
        try {
            // Load the JDBC driver
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Prepare the SQL statement to update the vendor details
            pst = conn.prepareStatement("UPDATE vendor SET ven_name=?, contact_info=?, address=?, items_supplied=? WHERE ven_id=?");
            pst.setString(1, vendor_name);
            pst.setLong(2, contact_info);
            pst.setString(3, address);
            pst.setString(4, items_supplied);
            pst.setInt(5, ven_id); // Set the vendor ID to locate the record
            
            // Execute the update query
            int updatedRows = pst.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("Vendor updated successfully.");
                response.sendRedirect("vendor"); // Redirect to the vendor management page after updating
            } else {
                System.out.println("No vendor found with ID: " + ven_id);
                response.sendRedirect("vendor"); // Redirect to an error page if the update fails
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendRedirect("vendor"); // Redirect to an error page in case of exception
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

