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

public class CustomerServlet extends HttpServlet {
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
        out.print("<div style='text-align:right;'><a href='deliveryboy'>Back to Home</a></div><br>");
        out.print("<html><body>");
        out.print("<h3>Welcome " + name + "</h3>");
        out.print("<h1>Customer Management</h1>");

        // Form for adding a new customer
        out.print("<h3>New Entry</h3>");
        out.print("<form action='customer' method='post'>");
        out.print("Customer Name: <input type='text' name='customerName' required><br><br>");
        out.print("Contact Info: <input type='number' name='contactInfo' required><br><br>");
        out.print("Address: <input type='text' name='address' required><br><br>");
        
        out.print("<button type='submit'>SAVE</button>");
        out.print("</form>");

        // Table for displaying customer details
        out.print("<h3>Customer Details</h3>");
        out.print("<table border='1'>");
        out.print("<tr><th>SLNO</th><th>Customer Name</th><th>Contact Info</th><th>Address</th><th>EDIT</th><th>DELETE</th></tr>");

        // Database operation to fetch customer details
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = conn.prepareStatement("SELECT * FROM customer");
             ResultSet rs = pst.executeQuery()) {

            int i = 1; // Serial number for each row
            while (rs.next()) {
                int cust_id = rs.getInt(1);
                String cust_name = rs.getString("cust_name");
                Long contact_info = rs.getLong("contact_info");
                String address = rs.getString("address");

                out.print("<tr>");
                out.print("<td>" + i + "</td>");
                out.print("<td>" + cust_name + "</td>");
                out.print("<td>" + contact_info + "</td>");
                out.print("<td>" + address + "</td>");
                out.print("<td><a href='editCustomer?id=" + cust_id + "'>Edit</a></td>");
                out.print("<td><a href='deleteCustomer?id=" + cust_id + "'>Delete</a></td>");
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
        String customerName = request.getParameter("customerName");
        String contactInfo = request.getParameter("contactInfo");
        String address = request.getParameter("address");

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String insertQuery = "INSERT INTO customer (cust_name, contact_info, address) VALUES (?, ?, ?)";
            try (PreparedStatement pst = conn.prepareStatement(insertQuery)) {
                pst.setString(1, customerName);
                pst.setLong(2, Long.parseLong(contactInfo)); // Assuming contactInfo is a number
                pst.setString(3, address);

                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    response.sendRedirect("customer"); // Redirect to the Customer Management page to see the updated list
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
