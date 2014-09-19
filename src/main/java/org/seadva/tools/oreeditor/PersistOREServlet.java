package org.seadva.tools.oreeditor;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dspace.foresite.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PersistOREServlet extends HttpServlet {

    private static Log log = LogFactory.getLog(PersistOREServlet.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            // get ResourceMap from session
            ResourceMap resourceMap = (ResourceMap) request.getSession().getAttribute("resource_map");
            // update the ResourceMap using form params
            boolean isUpdated = updateORE(resourceMap, request);
            // serialize the updated resource map
            ORESerialiser serial = ORESerialiserFactory.getInstance("RDF/XML");
            ResourceMapDocument doc = serial.serialise(resourceMap);
            String resourceMapXml = doc.toString();

            // write the ORE into a file
            String collectionId = (String) request.getSession().getAttribute("coll_id");
            String rand = UUID.randomUUID().toString();
            File outFile = new File(rand + "_" + collectionId + "_out_oaiore.xml");
            FileUtils.writeStringToFile(outFile, resourceMapXml);
            String outAbsolutePath = outFile.getAbsolutePath();
            log.info("Updated ORE written to: " + outAbsolutePath);

            // persist modified ORE through Registry API only if updated
            if (isUpdated) {
                OREUtils.persistORE(outAbsolutePath);
            }

            // forward the user to get_id UI
            RequestDispatcher dispatcher = request.getRequestDispatcher("/get_id.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            log.error("Error while forwarding response", e);
        }
    }

    private boolean updateORE(ResourceMap resourceMap, HttpServletRequest request) {
        boolean isUpdated = false;
        try {
            // get resource uri
            String resourceURI = resourceMap.getURI().toString();
            // get all predicates
            Aggregation aggregation = resourceMap.getAggregation();
            List<Triple> triples = aggregation.listTriples();
            // update triples according to request parameters
            isUpdated = updateTriples(aggregation, resourceURI, triples, request);

            // process aggregated resources
            List<AggregatedResource> aggregatedResources =
                    resourceMap.getAggregation().getAggregatedResources();
            for (AggregatedResource aggregatedResource : aggregatedResources) {
                List<URI> types = aggregatedResource.getTypes();
                // check whether there are child collections
                boolean childIsCollection = false;
                for (URI type : types) {
                    if (type.toString().contains("Aggregation")) {
                        childIsCollection = true;
                        // TODO: handle child collections
                        break;
                    }
                }
                if (childIsCollection) {
                    continue;
                }
                // get child resource URI
                String childURI = aggregatedResource.getURI().toString();
                // read predicates
                List<Triple> metadataTriples = aggregatedResource.listTriples();
                // update triples according to request parameters
                isUpdated = updateTriples(aggregation, childURI, metadataTriples, request);
            }
        } catch (Exception e) {
            log.error("Error while reading ORE. " + e);
        }
        return isUpdated;
    }

    private boolean updateTriples(Aggregation aggregation, String resourceURI, List<Triple> triples,
                                 HttpServletRequest request) throws OREException {
        boolean isUpdated = false;
        Map<String, Integer> arrayIndexes = new HashMap<String, Integer>();
        // update all triples of resource
        for(Triple metadataTriple: triples) {
            if (metadataTriple.getPredicate() == null) {
                continue;
            }
            if (metadataTriple.isLiteral()) {
                String predicateURI = metadataTriple.getPredicate().getURI().toString();
                String value = metadataTriple.getObjectLiteral().trim();
                String[] paramValues = request.getParameterValues(resourceURI + "__" + predicateURI);
                String parameter;
                if (paramValues.length == 0) {
                    continue;
                } else if (paramValues.length == 1) {
                    parameter = paramValues[0];
                    if (!value.equals(parameter)) {
                        aggregation.removeTriple(metadataTriple);
                        // update the predicate value with form value
                        metadataTriple.relate(metadataTriple.getPredicate(), parameter);
                        aggregation.addTriple(metadataTriple);
                        isUpdated = true;
                    }
                } else {
                    Integer index = arrayIndexes.get(predicateURI);
                    if (index == null) {
                        index = 0;
                        arrayIndexes.put(predicateURI, index);
                    }
                    parameter = paramValues[index];
                    arrayIndexes.put(predicateURI, index + 1);
                    aggregation.removeTriple(metadataTriple);
                    // update the predicate value with form value
                    metadataTriple.relate(metadataTriple.getPredicate(), parameter);
                    aggregation.addTriple(metadataTriple);
                    isUpdated = true;
                }
            }
        }
        return isUpdated;
    }

}
