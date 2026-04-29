package com.bloom;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/editItemAction")
public class EditItemAction extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/project";
    final String user = "root";
    final String pass = "amala1021";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve item details from the form submission
        int item_id = Integer.parseInt(request.getParameter("item_id"));
        String item_name = request.getParameter("item_name");
        int item_code = Integer.parseInt(request.getParameter("item_code")); // Parse as integer if item_code is numeric

        try {
            // Load the JDBC driver
            Class.forName(driver);
            try (Connection conn = DriverManager.getConnection(url, user, pass);
                 PreparedStatement pst = conn.prepareStatement("UPDATE item SET item_name=?, item_code=? WHERE item_id=?")) {

                // Prepare the SQL statement to update item details
                pst.setString(1, item_name);
                pst.setInt(2, item_code);
                pst.setInt(3, item_id);

                // Execute the update query
                int updatedRows = pst.executeUpdate();
                if (updatedRows > 0) {
                    System.out.println("Item updated successfully.");
                    response.sendRedirect("item"); // Redirect to item management page after updating
                } else {
                    System.out.println("No item found with ID: " + item_id);
                    response.sendRedirect("admin"); // Redirect to admin page if update fails
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendRedirect("error.jsp"); // Redirect to error page in case of exception
        }
    }
}


