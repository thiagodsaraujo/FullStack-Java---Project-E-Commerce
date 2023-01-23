package com.titashop.admin.category;

public class CategoryPageInfo {

    private  int totalPages;
    private  Long totalElements;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public Long setTotalElements(Long totalElements) {
       return this.totalElements = totalElements;
    }
}
