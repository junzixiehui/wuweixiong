package com.junzixiehui.zorm.query;

import java.io.Serializable;

/**
 * 分页信息 Created by cheshun on 2016/4/14.
 */
public class Pageable implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final int DEFAULT_PAGE_NUMBER = 1;

    private static final int DEFAULT_PAGE_SIZE = 20;

    private static final int MIN_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 1000;

    private int pageNumber;
    private int pageSize;

    public Pageable() {
        setPageNumber(DEFAULT_PAGE_NUMBER);
        setPageSize(DEFAULT_PAGE_SIZE);
    }

    public Pageable(int pageNumber) {
        setPageNumber(pageNumber);
    }

    public Pageable(int pageNumber, int pageSize) {
        setPageNumber(pageNumber);
        setPageSize(pageSize);
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        if (this.pageNumber < DEFAULT_PAGE_NUMBER) {
            this.pageNumber = DEFAULT_PAGE_NUMBER;
        }
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (this.pageSize < MIN_PAGE_SIZE) {
            this.pageSize = MIN_PAGE_SIZE;
        }
        if (this.pageSize > MAX_PAGE_SIZE) {
            this.pageSize = MAX_PAGE_SIZE;
        }
        this.pageSize = pageSize;
    }

}
