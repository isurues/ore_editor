package org.seadva.tools.oreeditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OREResource {

    private String uri;
    private Map<String, String> predicates;
    private List<OREResource> childResources;

    public OREResource() {
        this.predicates = new HashMap<String, String>();
        this.childResources = new ArrayList<OREResource>();
    }

    public void addPredicate(String predicate, String value) {
        this.predicates.put(predicate, value);
    }

    public void addChild(OREResource child) {
        this.childResources.add(child);
    }

    public Map<String, String> getPredicates() {
        return predicates;
    }

    public List<OREResource> getChildResources() {
        return childResources;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
