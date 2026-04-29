package com.bloom;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BillingServlet extends HttpServlet {
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
        out.print("<h1>Billing</h1>");

        out.print("<h3>New Entry</h3>");
        out.print("<form action='billing' method='post'>");
        out.print("Customer Name: <input type='text' name='customerName' required><br><br>");
        out.print("Total Amount: <input type='number' name='totalAmount' required><br><br>");
        out.print("Billing Date (dd/mm/yyyy): <input type='text' name='billingDate' required><br><br>");
        out.print("<button type='submit'>SAVE</button>");
        out.print("</form>");

        out.print("<h3>Billing Details</h3>");
        out.print("<table border='1'>");
        out.print("<tr><th>SLNO</th><th>Customer Name</th><th>Total Amount</th><th>Billing Date</th><th>EDIT</th><th>DELETE</th></tr>");

        try {
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, user, pass);
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM billing");
            ResultSet rs = pst.executeQuery();

            int i = 1;

            while (rs.next()) {
                int billing_id = rs.getInt("billing_id");
                String cust_name = rs.getString("cust_name");
                long total_amount = rs.getLong("total_amount");
                String billing_date = rs.getString("billing_date");

                out.print("<tr>");
                out.print("<td>" + i + "</td>");
                out.print("<td>" + cust_name + "</td>");
                out.print("<td>" + total_amount + "</td>");
                out.print("<td>" + billing_date + "</td>");
                out.print("<td><a href='editBilling?id=" + billing_id + "'>Edit</a></td>");
                out.print("<td><a href='deleteBilling?id=" + billing_id + "'>Delete</a></td>");
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
        String totalAmountStr = request.getParameter("totalAmount");
        String billingDate = request.getParameter("billingDate");

        long totalAmount;

        try {
            totalAmount = Long.parseLong(totalAmountStr);
        } catch (Exception e) {
            response.sendRedirect("error.jsp");
            return;
        }

        String formattedDate;

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = inputFormat.parse(billingDate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            formattedDate = outputFormat.format(date);

        } catch (Exception e) {
            response.sendRedirect("error.jsp");
            return;
        }

        try {
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, user, pass);

            String sql = "INSERT INTO billing (cust_name, total_amount, billing_date) VALUES (?, ?, ?)";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, customerName);
            pst.setLong(2, totalAmount);
            pst.setString(3, formattedDate);

            int rows = pst.executeUpdate();

            pst.close();
            conn.close();

            if (rows > 0) {
                response.sendRedirect("billing");
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
