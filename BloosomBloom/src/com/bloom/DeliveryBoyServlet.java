package com.bloom;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DeliveryBoyServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        if (session == null || !"delivery_boy".equals(session.getAttribute("role"))) {
            response.sendRedirect("index.html");
            return;
        }

        String uname = (String) session.getAttribute("uname");

        out.print("<html>");
        out.print("<head>");
        out.print("<title>Delivery Home</title>");

        out.print("<style>");

        out.print("body{");
        out.print("margin:0;");
        out.print("padding:0;");
        out.print("font-family:Arial,sans-serif;");
        out.print("background:linear-gradient(to right,#fbc2eb,#a6c1ee);");
        out.print("}");

        out.print(".header{");
        out.print("background:#6a0572;");
        out.print("color:white;");
        out.print("padding:25px;");
        out.print("text-align:center;");
        out.print("box-shadow:0 4px 10px rgba(0,0,0,0.2);");
        out.print("}");

        out.print(".header h1{margin:8px;}");
        out.print(".header h2{margin:8px; font-weight:normal;}");

        out.print(".menu{");
        out.print("width:400px;");
        out.print("margin:40px auto;");
        out.print("}");

        out.print(".menu a{");
        out.print("display:block;");
        out.print("margin:15px 0;");
        out.print("padding:15px;");
        out.print("text-decoration:none;");
        out.print("text-align:center;");
        out.print("font-size:20px;");
        out.print("background:white;");
        out.print("color:#6a0572;");
        out.print("border-radius:12px;");
        out.print("box-shadow:0 5px 12px rgba(0,0,0,0.15);");
        out.print("transition:0.3s;");
        out.print("}");

        out.print(".menu a:hover{");
        out.print("background:#6a0572;");
        out.print("color:white;");
        out.print("transform:scale(1.03);");
        out.print("}");

        out.print(".logout a{");
        out.print("background:#ef233c;");
        out.print("color:white;");
        out.print("}");

        out.print(".logout a:hover{");
        out.print("background:#d90429;");
        out.print("}");

        out.print("</style>");
        out.print("</head>");

        out.print("<body>");
        out.print("<div style='text-align:right;'><a href='index.html' style='color:#ff1493; font-weight:bold; text-decoration:none;'>Back to Home</a></div><br>");
        out.print("<div class='header'>");
        out.print("<h1>Welcome, " + uname + "!</h1>");
        out.print("<h2>Delivery Dashboard</h2>");
        out.print("</div>");

        out.print("<div class='menu'>");

        out.print("<a href='customer'>1. Customer</a>");
        out.print("<a href='stock'>2. Item Stock</a>");
        out.print("<a href='itempricedetails'>3. Item Price</a>");
        out.print("<a href='order'>4. Order</a>");
        out.print("<a href='billing'>5. Billing</a>");

        out.print("<div class='logout'>");
        out.print("<a href='signout'>Sign Out</a>");
        out.print("</div>");

        out.print("</div>");

        out.print("</body>");
        out.print("</html>");
    }
}