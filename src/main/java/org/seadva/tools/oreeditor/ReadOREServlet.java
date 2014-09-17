package org.seadva.tools.oreeditor;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.UUID;

public class ReadOREServlet extends HttpServlet {

    private static Log log = LogFactory.getLog(ReadOREServlet.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            // get collection id
            String id = request.getParameter("id");
            log.info("ID Accepted: " + id);

            // read ORE by calling the registry REST API
            WebResource webResource =  Client.create().resource(OREUtils.RO_URL);
            ClientResponse roResponse = webResource.path("resource").path("ro")
                    .path(URLEncoder.encode(id)).get(ClientResponse.class);

            // write the ORE into a file
            String rand = UUID.randomUUID().toString();
            String orePath = rand + "_oaiore.xml";
            IOUtils.copy(roResponse.getEntityInputStream(), new FileOutputStream(orePath));

            String temp = new java.io.File(orePath).getAbsolutePath();
            log.info("ORE Path: " + temp);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/ore.jsp");
            // set resource map details
            request.setAttribute("resource_map", id);
            dispatcher.forward(request, response);
        } catch (Exception e) {
            log.error("Error while forwarding response", e);
        }

    }

}
