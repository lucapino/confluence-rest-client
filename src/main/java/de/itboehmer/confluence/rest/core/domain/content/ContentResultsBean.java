package de.itboehmer.confluence.rest.core.domain.content;

import com.google.gson.annotations.Expose;
import de.itboehmer.confluence.rest.core.domain.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Christian Schulze (c.schulze@micromata.de)
 * Date: 04.07.2016
 * Project: ConfluenceTransferPlugin
 */
public class ContentResultsBean extends BaseBean {

    @Expose
    private List<ContentBean> results = new ArrayList<>();
    
    @Expose
    private Integer start;
    
    @Expose
    private Integer limit;
    
    @Expose
    private Integer size;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public List<ContentBean> getResults() {
        return results;
    }

    public void setResults(List<ContentBean> results) {
        this.results = results;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }
}
