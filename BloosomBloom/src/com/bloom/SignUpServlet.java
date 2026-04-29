package com.bloom;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SignUpServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/project";
    final String user = "root";
    final String pass = "amala1021";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Get values from the signup form
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        System.out.println("Username: " + username); // Debugging
        System.out.println("Password: " + password); // Debugging
        System.out.println("Role: " + role); // Debugging

        if (!"admin".equalsIgnoreCase(role) && !"delivery_boy".equalsIgnoreCase(role)) {
            out.print("Invalid role. Only 'admin' and 'deliveryboy' are allowed.");
            RequestDispatcher dis = request.getRequestDispatcher("SignUp.html");
            dis.include(request, response);
            return;
        }

        Connection conn = null;
        PreparedStatement pst = null;

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "INSERT INTO login (user_name, user_pass, user_role) VALUES (?, ?, ?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, role);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                out.print("Signup successful! You can now log in.");
                response.sendRedirect("index.html");  // Redirect after successful signup
            } else {
                out.print("Signup failed. Please try again.");
                RequestDispatcher dis = request.getRequestDispatcher("SignUp.html");
                dis.include(request, response);
            }

        } catch (ClassNotFoundException e) {
            out.print("JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            out.print("Database connection error.");
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
