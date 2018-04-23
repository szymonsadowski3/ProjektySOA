package org.jboss.as.quickstarts.jms;

import javax.jms.JMSException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;

@WebServlet(name = "MainServlet")
public class MainServlet extends HttpServlet {
    private QueueManager mngr = new QueueManager();

    public MainServlet() throws JMSException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String phoneNum = request.getParameter("phoneNum");

        mngr.putMessageInQueue(MessageFormat.format("Name: {0} | Address: {1} | Phone Number: {2}", name, address, phoneNum));

        RequestDispatcher view = request.getRequestDispatcher("/listOfCompanies.html");
        view.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher view = request.getRequestDispatcher("/listOfCompanies.html");
        view.forward(request, response);
    }
}
