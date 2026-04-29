package com.bloom;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SignInServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/project";
    final String user = "root";
    final String pass = "amala1021";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "SELECT user_role FROM login WHERE user_name = ? AND user_pass = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);

            rs = pst.executeQuery();

            if (rs.next()) {
                String role = rs.getString("user_role");

                HttpSession session = request.getSession();
                session.setAttribute("uname", username);
                session.setAttribute("role", role);

                if ("admin".equalsIgnoreCase(role)) {
                    response.sendRedirect("admin");
                } else if ("delivery_boy".equalsIgnoreCase(role)) {
                    response.sendRedirect("deliveryboy");
                } else {
                    out.print("Unauthorized role.");
                    RequestDispatcher dis = request.getRequestDispatcher("index.html");
                    dis.include(request, response);
                }
            } else {
                out.print("Invalid username or password.");
                RequestDispatcher dis = request.getRequestDispatcher("index.html");
                dis.include(request, response);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            out.print("Driver not found.");
        } catch (SQLException e) {
            e.printStackTrace();
            out.print("Database error.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

