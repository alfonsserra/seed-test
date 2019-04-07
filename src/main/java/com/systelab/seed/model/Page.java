package com.systelab.seed.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Page<T> implements Serializable {

    private long totalElements;

    private List<T> content;

    public Page() {
        this.content = new ArrayList();
        this.totalElements = 0;
    }

    public Page(List<T> content, long total) {
        this.content = content;
        this.totalElements = total;
    }
}