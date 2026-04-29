package com.bloom;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/deleteStock")
public class DeleteStock extends HttpServlet {

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

        String id = request.getParameter("id");

        Connection conn = null;
        PreparedStatement pst = null;

        try {
            // Load JDBC driver
            Class.forName(driver);

            conn = DriverManager.getConnection(url, user, pass);

            String sql = "DELETE FROM stock WHERE stock_id = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, id);

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                out.print("Stock item with ID " + id + " deleted successfully.");
            } else {
                out.print("No stock item found with ID: " + id);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            out.print("Error deleting stock item.");
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
