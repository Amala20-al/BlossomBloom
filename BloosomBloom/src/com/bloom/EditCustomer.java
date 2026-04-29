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

@WebServlet("/editCustomer")
public class EditCustomer extends HttpServlet {

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

        String id = request.getParameter("id"); // Vendor ID

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            // Load JDBC driver
            Class.forName(driver);

            conn = DriverManager.getConnection(url, user, pass);

            // SQL query to fetch vendor details based on the vendor ID
            String sql = "SELECT * FROM customer WHERE cust_id = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, id);

            rs = pst.executeQuery();

            if (rs.next()) {
                // Display the vendor details in the form for editing
                out.print("<form name='editCustomer' method='post' action='editCustomerAction'>");
                out.print("Customer ID: <input type='hidden' name='cust_id' value='" + rs.getInt("cust_id") + "'><br>");
                out.print("Customer Name: <input type='text' name='customerName' value='" + rs.getString("cust_name") + "'><br>");
                out.print("Contact Info: <input type='text' name='contactInfo' value='" + rs.getLong("contact_info") + "'><br>");
                out.print("Address: <input type='text' name='address' value='" + rs.getString("address") + "'><br>");
                
                out.print("<input type='submit' value='EDIT'>");  // Submit button
                out.print("</form>");
            } else {
                out.print("Customer not found with ID: " + id);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            out.print("Error retrieving vendor details.");
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

