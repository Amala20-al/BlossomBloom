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

@WebServlet("/editReportAction")
public class EditReportAction extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Database configuration details
    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/project";
    final String user = "root";
    final String pass = "amala1021";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Retrieve the report details from the form submission
        int report_id = Integer.parseInt(request.getParameter("report_id"));
        String report_type = request.getParameter("reportType");
        String date_generated = request.getParameter("dateGenerated");

        System.out.println("Updating report with ID: " + report_id);
        System.out.println("Report Type: " + report_type);
        System.out.println("Date Generated: " + date_generated);

        try {
            // Load the JDBC driver and establish a connection in a try-with-resources block
            Class.forName(driver);
            try (Connection conn = DriverManager.getConnection(url, user, pass);
                 PreparedStatement pst = conn.prepareStatement("UPDATE report SET report_type=?, date_generated=? WHERE report_id=?")) {
                
                pst.setString(1, report_type);
                pst.setString(2, date_generated);
                pst.setInt(3, report_id);

                int updatedRows = pst.executeUpdate();
                if (updatedRows > 0) {
                    System.out.println("Report updated successfully.");
                    response.sendRedirect("report"); // Redirect to the report page after updating
                } else {
                    System.out.println("No report record found with ID: " + report_id);
                    response.sendRedirect("error.jsp?message=No+report+record+found"); // Redirect to an error page if the update fails
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendRedirect("error.jsp?message=Database+update+error"); // Redirect to an error page in case of exception
        }
    }
}
