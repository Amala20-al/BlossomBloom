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

@WebServlet("/editOrderAction")
public class EditOrderAction extends HttpServlet {

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
        
        // Retrieve the order details from the form submission
        int order_id = Integer.parseInt(request.getParameter("order_id"));
        String cust_name = request.getParameter("customerName");
        String flower = request.getParameter("flower");
        String quantity = request.getParameter("quantity");
        String delivery_status = request.getParameter("deliveryStatus");
        
        // Debugging output to console
        System.out.println("Updating order with ID: " + order_id);
        System.out.println("Customer Name: " + cust_name);
        System.out.println("Flower: " + flower);
        System.out.println("Quantity: " + quantity);
        System.out.println("Delivery Status: " + delivery_status);
        
        try {
            // Load the JDBC driver
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Prepare the SQL statement to update the order details
            pst = conn.prepareStatement("UPDATE `order` SET cust_name=?, flower=?, quantity=?, delivery_status=? WHERE order_id=?");
            pst.setString(1, cust_name);
            pst.setString(2, flower);
            pst.setString(3, quantity);
            pst.setString(4, delivery_status);
            pst.setInt(5, order_id); // Set the order ID to locate the record
            
            // Execute the update query
            int updatedRows = pst.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("Order updated successfully.");
                response.sendRedirect("order"); // Redirect to the order management page after updating
            } else {
                System.out.println("No order found with ID: " + order_id);
                response.sendRedirect("order"); // Redirect to an error page if the update fails
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
