package com.example.resource.dto;

import com.example.resource.entities.Return;

import java.util.List;

public class ReturnSearchResponse {

    private long hits;

    private List<Return> returns;

    public ReturnSearchResponse() {
    }

    public ReturnSearchResponse(long hits, List<Return> returns) {
        this.hits = hits;
        this.returns = returns;
    }

    public long getHits() {
        return hits;
    }

    public void setHits(long hits) {
        this.hits = hits;
    }

    public List<Return> getReturns() {
        return returns;
    }

    public void setReturns(List<Return> returns) {
        this.returns = returns;
    }
}
