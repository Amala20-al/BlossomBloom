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

@WebServlet("/editBilling")
public class EditBilling extends HttpServlet {

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

        String id = request.getParameter("id"); // Billing ID

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            // Load JDBC driver
            Class.forName(driver);

            conn = DriverManager.getConnection(url, user, pass);

            // SQL query to fetch billing details based on the billing ID
            String sql = "SELECT * FROM billing WHERE billing_id = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, id);

            rs = pst.executeQuery();

            if (rs.next()) {
            	
                // Display the billing details in the form for editing
                out.print("<form name='editBilling' method='post' action='editBillingAction'>");
                out.print("Billing ID: <input type='hidden' name='billing_id' value='" + rs.getInt("billing_id") + "'><br>");
                out.print("Customer Name: <input type='text' name='customerName' value='" + rs.getString("cust_name") + "'><br>");
                out.print("Total Amount: <input type='text' name='totalAmount' value='" + rs.getLong("total_amount") + "'><br>");
                out.print("Billing Date (dd/mm/yyyy): <input type='text' name='billingDate' value='" + rs.getString("billing_date") + "'><br>");
                out.print("<input type='submit' value='EDIT'>");  // Submit button
                out.print("</form>");
            } else {
                out.print("Billing record not found with ID: " + id);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            out.print("Error retrieving billing details.");
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
