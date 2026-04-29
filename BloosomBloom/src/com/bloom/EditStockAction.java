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

@WebServlet("/editStockAction")
public class EditStockAction extends HttpServlet {

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
        
        // Retrieve the stock details from the form submission
        int stock_id = Integer.parseInt(request.getParameter("stock_id"));
        String item_name = request.getParameter("itemName");
        String vendor_name = request.getParameter("vendorName");
        String quantity = request.getParameter("quantity");
        
        // Debugging output to console
        System.out.println("Updating stock with ID: " + stock_id);
        System.out.println("New Item Name: " + item_name);
        System.out.println("New Vendor Name: " + vendor_name);
        System.out.println("New Quantity: " + quantity);
        
        try {
            // Load the JDBC driver
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Prepare the SQL statement to update the stock details
            pst = conn.prepareStatement("UPDATE stock SET item_name=?, vendor_name=?, quantity=? WHERE stock_id=?");
            pst.setString(1, item_name);
            pst.setString(2, vendor_name);
            pst.setString(3, quantity);
            pst.setInt(4, stock_id); // Set the stock ID to locate the record
            
            // Execute the update query
            int updatedRows = pst.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("Stock updated successfully.");
                response.sendRedirect("stock"); // Redirect to the stock management page after updating
            } else {
                System.out.println("No stock found with ID: " + stock_id);
                response.sendRedirect("stock"); // Redirect to an error page if the update fails
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

