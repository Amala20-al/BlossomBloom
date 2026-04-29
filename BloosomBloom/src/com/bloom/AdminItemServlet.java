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

public class AdminItemServlet extends HttpServlet {
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
            out.print("<div style='text-align:right;'><a href='delivery_boy'>Back to Home</a></div><br>");
        }

        out.print("<h3>Welcome " + name + "</h3>");
        out.print("<h1>Item Management</h1>");

        out.print("<h3>New Item Entry</h3>");
        out.print("<form action='item' method='post'>");
        out.print("Item Name: <input type='text' name='itemName'><br><br>");
        out.print("Item Code: <input type='number' name='itemCode'><br><br>");
        out.print("<button type='submit'>SAVE</button>");
        out.print("</form>");

        out.print("<h3>Item Details</h3>");
        out.print("<table border='1'>");
        out.print("<tr><th>SLNO</th><th>Item Name</th><th>Item Code</th><th>EDIT</th><th>DELETE</th></tr>");

        try {
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, user, pass);
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM item");
            ResultSet rs = pst.executeQuery();

            int i = 1;

            while (rs.next()) {
                int item_id = rs.getInt("item_id");
                String item_name = rs.getString("item_name");
                int item_code = rs.getInt("item_code");

                out.print("<tr>");
                out.print("<td>" + i + "</td>");
                out.print("<td>" + item_name + "</td>");
                out.print("<td>" + item_code + "</td>");
                out.print("<td><a href='editItem?id=" + item_id + "'>Edit</a></td>");
                out.print("<td><a href='deleteItem?id=" + item_id + "'>Delete</a></td>");
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
        int itemCode = Integer.parseInt(request.getParameter("itemCode"));

        try {
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, user, pass);

            String sql = "INSERT INTO item (item_name, item_code) VALUES (?, ?)";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, itemName);
            pst.setInt(2, itemCode);

            int rows = pst.executeUpdate();

            pst.close();
            conn.close();

            HttpSession session = request.getSession(false);
            String role = (String) session.getAttribute("role");

            if (rows > 0) {
                response.sendRedirect("item");
            } else {
                if ("admin".equals(role)) {
                    response.sendRedirect("admin");
                } else {
                    response.sendRedirect("delivery_boy");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
