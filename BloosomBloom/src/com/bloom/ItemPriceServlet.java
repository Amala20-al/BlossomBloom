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

public class ItemPriceServlet extends HttpServlet {
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
        out.print("<h1>Item Price Management</h1>");

        out.print("<h3>Item Price Entry</h3>");
        out.print("<form action='itempricedetails' method='post'>");
        out.print("Item Name: <input type='text' name='itemName' required><br><br>");
        out.print("Item Price: <input type='number' name='itemPrice' required><br><br>");
        out.print("<button type='submit'>SAVE</button>");
        out.print("</form>");

        out.print("<h3>Item Details</h3>");
        out.print("<table border='1'>");
        out.print("<tr><th>SLNO</th><th>Item Name</th><th>Item Price</th><th>EDIT</th><th>DELETE</th></tr>");

        try {
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, user, pass);
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM itemprice");
            ResultSet rs = pst.executeQuery();

            int i = 1;

            while (rs.next()) {
                int item_id = rs.getInt("item_id");
                String item_name = rs.getString("item_name");
                int item_price = rs.getInt("item_price");

                out.print("<tr>");
                out.print("<td>" + i + "</td>");
                out.print("<td>" + item_name + "</td>");
                out.print("<td>" + item_price + "</td>");
                out.print("<td><a href='editItemPrice?id=" + item_id + "'>Edit</a></td>");
                out.print("<td><a href='deleteItemPrice?id=" + item_id + "'>Delete</a></td>");
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
        int itemPrice = Integer.parseInt(request.getParameter("itemPrice"));

        try {
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, user, pass);

            String sql = "INSERT INTO itemprice (item_name, item_price) VALUES (?, ?)";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, itemName);
            pst.setInt(2, itemPrice);

            int rows = pst.executeUpdate();

            pst.close();
            conn.close();

            if (rows > 0) {
                response.sendRedirect("itempricedetails");
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