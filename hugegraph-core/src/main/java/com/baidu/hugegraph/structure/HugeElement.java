/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.hugegraph.structure;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.tinkerpop.gremlin.structure.Element;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.apache.tinkerpop.gremlin.structure.util.ElementHelper;

/**
 * Created by zhangsuochao on 17/2/6.
 */
public abstract class HugeElement implements Element {
    protected final Object id;
    protected final String label;
    private final Graph graph;

    protected Long createdAt;
    protected Long updatedAt;
    protected Map<String, Object> properties;

    public enum ElementType {
        EDGE,
        VERTEX
    }

    public HugeElement(final Graph graph, final Object id,final String label){
        this.graph = graph;
        this.id = id;
        this.label = label;
    }

    @Override
    public Object id() {
        return this.id;
    }

    @Override
    public String label() {
        return this.label;
    }

    @Override
    public Graph graph() {
        return this.graph;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Object... keyValues) {
        ElementHelper.legalPropertyKeyValueArray(keyValues);
        if(this.properties == null){
            this.properties = new HashMap<>();
        }
        for (int i = 0; i < keyValues.length; i = i + 2) {
            if (!keyValues[i].equals(T.id) && !keyValues[i].equals(T.label))
                this.properties.put((String) keyValues[i], keyValues[i + 1]);
        }
    }

    public void setPropertyMap(Map<String,Object> map){
        this.properties = map;
    }
}
