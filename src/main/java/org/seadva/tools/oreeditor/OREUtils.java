package org.seadva.tools.oreeditor;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dspace.foresite.Predicate;

import javax.ws.rs.core.MediaType;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.UUID;

public class OREUtils {

    private static Log log = LogFactory.getLog(ReadOREServlet.class);
    // TODO : Use a config file for this
    // public static String RO_URL = "http://localhost:8080/ro/";
    public static String RO_URL = "http://seadva.d2i.indiana.edu/ro/";
    public static Predicate METS_LOCATION;

    public static int ORE_SUCCESS = 0;
    public static int ORE_FAILURE = 1;
    public static int ORE_NO_UPDATE = 2;

    static {
        try {
            METS_LOCATION = new Predicate();
            METS_LOCATION.setNamespace("http://www.loc.gov/METS");
            METS_LOCATION.setPrefix("http://www.loc.gov/METS");
            METS_LOCATION.setName("FLocat");
            METS_LOCATION.setURI(new URI("http://www.loc.gov/METS/FLocat"));
        } catch (URISyntaxException e) {
            log.error("Error while initializing METS_LOCATION predicate", e);
        }
    }

    public static int persistORE(String orePath) {
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
            return ORE_FAILURE;
        }
        return ORE_SUCCESS;
    }

    public static String readORE(String collectionURI, String collectionId) {
        String absolutePath = null;
        try {
            // read ORE by calling the registry REST API
            WebResource webResource =  Client.create().resource(OREUtils.RO_URL);
            ClientResponse roResponse = webResource.path("resource").path("ro")
                    .path(URLEncoder.encode(collectionURI)).get(ClientResponse.class);

            // write the ORE into a file
            String rand = UUID.randomUUID().toString();
            String orePath = rand + "_" + collectionId + "_in_oaiore.xml";
            IOUtils.copy(roResponse.getEntityInputStream(), new FileOutputStream(orePath));

            absolutePath = new java.io.File(orePath).getAbsolutePath();
            log.info("ORE Path: " + absolutePath);
        } catch (IOException e) {
            log.error("Error while reading ORE: " + collectionURI, e);
        }
        return absolutePath;
    }

}
