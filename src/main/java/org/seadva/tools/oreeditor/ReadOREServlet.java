package org.seadva.tools.oreeditor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dspace.foresite.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

public class ReadOREServlet extends HttpServlet {

    private static Log log = LogFactory.getLog(ReadOREServlet.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        handleGetPost(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        handleGetPost(request, response);
    }

    private void handleGetPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            // get collection id
            String collectionURI = request.getParameter("id");
            String collectionId = collectionURI.substring(collectionURI.lastIndexOf('/') + 1);
            log.info("ID Accepted: " + collectionURI);
            // read ORE from registry REST API
            String orePath = OREUtils.readORE(collectionURI, collectionId);

            InputStream input = new FileInputStream(orePath);
            OREParser parser = OREParserFactory.getInstance("RDF/XML");
            ResourceMap resourceMap = parser.parse(input);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/ore.jsp");
            // set OREResource as a request attribute
            request.setAttribute("ore_resource", parseORE(resourceMap));
            // set ResourceMap to session
            request.getSession().setAttribute("resource_map", resourceMap);
            request.getSession().setAttribute("coll_id", collectionId);
            dispatcher.forward(request, response);
        } catch (Exception e) {
            log.error("Error while forwarding response", e);
        }
    }

    private OREResource parseORE(ResourceMap resourceMap) {
        OREResource resource = new OREResource();
        try {
            // get resource uri
            String resourceURI = resourceMap.getURI().toString();
            resource.setUri(resourceURI);
            // get all predicates
            List<Triple> triples = resourceMap.getAggregation().listTriples();
            for (Triple triple : triples) {
                if (triple.getPredicate() == null) {
                    continue;
                }
                String predicate = triple.getPredicate().getURI().toString();
                if (triple.isLiteral()) {
                    String value = triple.getObjectLiteral();
                    resource.addPredicate(predicate, value);
                }
            }
            // check whether there are child collections
            List<AggregatedResource> aggregatedResources =
                    resourceMap.getAggregation().getAggregatedResources();
            for (AggregatedResource aggregatedResource : aggregatedResources) {
                List<URI> types = aggregatedResource.getTypes();
                boolean childIsCollection = false;
                for (URI type : types) {
                    if (type.toString().contains("Aggregation")) {
                        TripleSelector locSelector = new TripleSelector();
                        locSelector.setSubjectURI(aggregatedResource.getURI());
                        locSelector.setPredicate(OREUtils.METS_LOCATION);
                        List<Triple> locTriples = resourceMap.getAggregation().listAllTriples(locSelector);

                        if (locTriples.size() > 0) {
                            for (Triple locTriple : locTriples) {
                                String oreFilePath = locTriple.getObjectLiteral();
                                OREParser parser = OREParserFactory.getInstance("RDF/XML");
                                ResourceMap rem = parser.parse(new FileInputStream(oreFilePath));
                                resource.addChild(parseORE(rem));
                            }
                        }
                        childIsCollection = true;
                        break;
                    }
                }
                if (childIsCollection) {
                    continue;
                }

                // create new resource
                OREResource childResource = new OREResource();
                childResource.setUri(aggregatedResource.getURI().toString());
                // read predicates
                List<Triple> metadataTriples = aggregatedResource.listTriples();

                for (Triple metadataTriple: metadataTriples) {
                    if (metadataTriple.getPredicate() == null) {
                        continue;
                    }
                    if (metadataTriple.isLiteral()) {
                        String predicateUri = metadataTriple.getPredicate().getURI().toString();
                        String value = metadataTriple.getObjectLiteral();
                        childResource.addPredicate(predicateUri, value);
                    }
                }
                // add child resource to parent
                resource.addChild(childResource);
            }

        } catch (Exception e) {
            log.error("Error while reading ORE. " + e);
        }
        return resource;
    }

}
