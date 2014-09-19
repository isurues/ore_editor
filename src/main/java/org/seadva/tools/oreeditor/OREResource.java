package org.seadva.tools.oreeditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OREResource {

    private String uri;
    private Map<String, List<String>> predicates;
    private List<OREResource> childResources;

    public OREResource() {
        this.predicates = new HashMap<String, List<String>>();
        this.childResources = new ArrayList<OREResource>();
    }

    public void addPredicate(String predicate, String value) {
        List<String> valueList = this.predicates.get(predicate);
        if (valueList == null) {
            valueList = new ArrayList<String>();
            this.predicates.put(predicate, valueList);
        }
        valueList.add(value);
    }

    public void addChild(OREResource child) {
        this.childResources.add(child);
    }

    public Map<String, List<String>> getPredicates() {
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
