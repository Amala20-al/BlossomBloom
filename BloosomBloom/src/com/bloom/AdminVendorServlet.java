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

public class AdminVendorServlet extends HttpServlet {
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

        if (session == null || session.getAttribute("uname") == null) {
            response.sendRedirect("index.html");
            return;
        }

        String name = (String) session.getAttribute("uname");

        out.print("<html><body>");
        out.print("<div style='text-align:right;'><a href='admin'>Back to Home</a></div><br>");
        out.print("<h3>Welcome " + name + "</h3>");
        out.print("<h1>Vendor Management</h1>");

        out.print("<h3>New Entry</h3>");
        out.print("<form action='vendor' method='post'>");
        out.print("Vendor Name: <input type='text' name='vendorName'><br><br>");
        out.print("Contact Info: <input type='number' name='contactInfo'><br><br>");
        out.print("Address: <input type='text' name='address'><br><br>");
        out.print("Items Supplied: <input type='text' name='itemSupply'><br><br>");
        out.print("<button type='submit'>SAVE</button>");
        out.print("</form>");

        out.print("<h3>Vendor Details</h3>");
        out.print("<table border='1'>");
        out.print("<tr><th>SLNO</th><th>Vendor Name</th><th>Contact Info</th><th>Address</th><th>Items Supplied</th><th>Edit</th><th>Delete</th></tr>");

        try {
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, user, pass);
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM vendor");
            ResultSet rs = pst.executeQuery();

            int i = 1;

            while (rs.next()) {
                int ven_id = rs.getInt("ven_id");
                String ven_name = rs.getString("ven_name");
                long contact_info = rs.getLong("contact_info");
                String address = rs.getString("address");
                String items_supplied = rs.getString("items_supplied");

                out.print("<tr>");
                out.print("<td>" + i + "</td>");
                out.print("<td>" + ven_name + "</td>");
                out.print("<td>" + contact_info + "</td>");
                out.print("<td>" + address + "</td>");
                out.print("<td>" + items_supplied + "</td>");
                out.print("<td><a href='editVendor?id=" + ven_id + "'>Edit</a></td>");
                out.print("<td><a href='deleteVendor?id=" + ven_id + "'>Delete</a></td>");
                out.print("</tr>");

                i++;
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (Exception e) {
            out.print("<p>Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }

        out.print("</table>");
        out.print("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String vendorName = request.getParameter("vendorName");
        String contactInfo = request.getParameter("contactInfo");
        String address = request.getParameter("address");
        String itemsSupplied = request.getParameter("itemSupply");

        try {
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, user, pass);

            String sql = "INSERT INTO vendor (ven_name, contact_info, address, items_supplied) VALUES (?, ?, ?, ?)";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, vendorName);
            pst.setLong(2, Long.parseLong(contactInfo));
            pst.setString(3, address);
            pst.setString(4, itemsSupplied);

            int rows = pst.executeUpdate();

            pst.close();
            conn.close();

            if (rows > 0) {
                response.sendRedirect("vendor");
            } else {
                response.sendRedirect("admin");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
