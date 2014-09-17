package org.seadva.tools.oreeditor;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.core.MediaType;

public class OREUtils {

    private static Log log = LogFactory.getLog(ReadOREServlet.class);
    public static String RO_URL = "http://localhost:8080/ro/";

    public static void persistORE(String orePath) {
        WebResource webResource = Client.create().resource(RO_URL);
        java.io.File file = new java.io.File(orePath);
        FileDataBodyPart fdp = new FileDataBodyPart("file", file,
                MediaType.APPLICATION_OCTET_STREAM_TYPE);

        FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
        formDataMultiPart.bodyPart(fdp);
        ClientResponse response = webResource.path("resource")
                .path("putro")
                .type(MediaType.MULTIPART_FORM_DATA)
                .post(ClientResponse.class, formDataMultiPart);
        if (response.getStatus() != 200) {
            log.error("Bad Response on ORE persist..");
        }
    }

}
