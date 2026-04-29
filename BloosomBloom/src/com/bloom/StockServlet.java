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

public class StockServlet extends HttpServlet {
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
        out.print("<h1>Stock Management</h1>");

        out.print("<h3>New Entry</h3>");
        out.print("<form action='stock' method='post'>");
        out.print("Item Name: <input type='text' name='itemName'><br><br>");
        out.print("Vendor Name: <input type='text' name='vendorName'><br><br>");
        out.print("Quantity: <input type='text' name='quantity'><br><br>");
        out.print("<button type='submit'>SAVE</button>");
        out.print("</form>");

        out.print("<h3>Stock Details</h3>");
        out.print("<table border='1'>");
        out.print("<tr><th>SLNO</th><th>Item Name</th><th>Vendor Name</th><th>Quantity</th><th>EDIT</th><th>DELETE</th></tr>");

        try {
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, user, pass);
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM stock");
            ResultSet rs = pst.executeQuery();

            int i = 1;

            while (rs.next()) {
                int stock_id = rs.getInt("stock_id");
                String item_name = rs.getString("item_name");
                String vendor_name = rs.getString("vendor_name");
                String quantity = rs.getString("quantity");

                out.print("<tr>");
                out.print("<td>" + i + "</td>");
                out.print("<td>" + item_name + "</td>");
                out.print("<td>" + vendor_name + "</td>");
                out.print("<td>" + quantity + "</td>");
                out.print("<td><a href='editStock?id=" + stock_id + "'>Edit</a></td>");
                out.print("<td><a href='deleteStock?id=" + stock_id + "'>Delete</a></td>");
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

        String itemName = request.getParameter("itemName");
        String vendorName = request.getParameter("vendorName");
        String quantity = request.getParameter("quantity");

        try {
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, user, pass);

            String sql = "INSERT INTO stock (item_name, vendor_name, quantity) VALUES (?, ?, ?)";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, itemName);
            pst.setString(2, vendorName);
            pst.setString(3, quantity);

            int rows = pst.executeUpdate();

            pst.close();
            conn.close();

            if (rows > 0) {
                response.sendRedirect("stock");
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


