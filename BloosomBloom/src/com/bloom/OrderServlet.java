package com.bloom;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class OrderServlet extends HttpServlet {
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

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("index.html");
            return;
        }

        String role = (String) session.getAttribute("role");
        String name = (String) session.getAttribute("uname");

        out.print("<html><body>");

        if ("admin".equals(role)) {
            out.print("<div style='text-align:right;'><a href='admin'>Back to Home</a></div><br>");
        } else {
            out.print("<div style='text-align:right;'><a href='deliveryboy'>Back to Home</a></div><br>");
        }

        out.print("<h3>Welcome " + name + "</h3>");
        out.print("<h1>Order Management</h1>");

        out.print("<h3>New Entry</h3>");
        out.print("<form action='order' method='post'>");
        out.print("Customer Name: <input type='text' name='customerName' required><br><br>");
        out.print("Flower: <input type='text' name='flower' required><br><br>");
        out.print("Quantity: <input type='text' name='quantity' required><br><br>");
        out.print("Delivery Status: <input type='text' name='deliveryStatus' required><br><br>");
        out.print("<button type='submit'>SAVE</button>");
        out.print("</form>");

        out.print("<h3>Order Details</h3>");
        out.print("<table border='1'>");
        out.print("<tr><th>SLNO</th><th>Customer Name</th><th>Flower</th><th>Quantity</th><th>Delivery Status</th><th>EDIT</th><th>DELETE</th></tr>");

        try {
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, user, pass);
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM `order`");
            ResultSet rs = pst.executeQuery();

            int i = 1;

            while (rs.next()) {
                int order_id = rs.getInt("order_id");
                String cust_name = rs.getString("cust_name");
                String flower = rs.getString("flower");
                String quantity = rs.getString("quantity");
                String delivery_status = rs.getString("delivery_status");

                out.print("<tr>");
                out.print("<td>" + i + "</td>");
                out.print("<td>" + cust_name + "</td>");
                out.print("<td>" + flower + "</td>");
                out.print("<td>" + quantity + "</td>");
                out.print("<td>" + delivery_status + "</td>");
                out.print("<td><a href='editOrder?id=" + order_id + "'>Edit</a></td>");
                out.print("<td><a href='deleteOrder?id=" + order_id + "'>Delete</a></td>");
                out.print("</tr>");

                i++;
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (Exception e) {
            out.print("Error: " + e.getMessage());
        }

        out.print("</table>");
        out.print("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String customerName = request.getParameter("customerName");
        String flower = request.getParameter("flower");
        String quantity = request.getParameter("quantity");
        String deliveryStatus = request.getParameter("deliveryStatus");

        try {
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, user, pass);

            String sql = "INSERT INTO `order` (cust_name, flower, quantity, delivery_status) VALUES (?, ?, ?, ?)";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, customerName);
            pst.setString(2, flower);
            pst.setString(3, quantity);
            pst.setString(4, deliveryStatus);

            int rows = pst.executeUpdate();

            pst.close();
            conn.close();

            if (rows > 0) {
                response.sendRedirect("order");
            } else {
                HttpSession session = request.getSession(false);
                String role = (String) session.getAttribute("role");

                if ("admin".equals(role)) {
                    response.sendRedirect("admin");
                } else {
                    response.sendRedirect("deliveryboy");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}