package org.seadva.tools.oreeditor;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ReadOREServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            // get collection id
            String id = request.getParameter("id");

            // read ORE by calling the registry REST API

            RequestDispatcher dispatcher = request.getRequestDispatcher("/ore.jsp");
            // set resource map details
            request.setAttribute("resource_map", id);
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
