package com.systelab.seed.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page<T> implements Serializable {

    public long totalElements;

    public List<T> content;

    public Page() {
        this.content = new ArrayList();
        this.totalElements = 0;
    }

    public Page(List<T> content, long total) {
        this.content = content;
        this.totalElements = total;
    }
}