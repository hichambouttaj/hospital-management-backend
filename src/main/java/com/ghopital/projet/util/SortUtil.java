package com.ghopital.projet.util;

import com.ghopital.projet.dto.response.PageDtoResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.List;

public class SortUtil {
    public static PageRequest buildPageRequest(
            Integer pageNumber,
            Integer pageSize,
            String sortField,
            String sortDirection) {
        int queryPageNumber;
        int queryPageSize;
        String querySortField;
        String querySortDirection;

        if(pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        }else {
            queryPageNumber = Constants.DEFAULT_PAGE_NUMBER;
        }

        if(pageSize == null) {
            queryPageSize = Constants.DEFAULT_PAGE_SIZE;
        }else {
            if(pageSize > 100) {
                queryPageSize = 100;
            }else {
                queryPageSize = pageSize;
            }
        }

        if(!StringUtils.hasText(sortField)) {
            querySortField = Constants.DEFAULT_SORT_FIELD;
        }else {
            querySortField = sortField;
        }

        if(!StringUtils.hasText(sortDirection)) {
            querySortDirection = Constants.DEFAULT_SORT_DIRECTION;
        }else {
            querySortDirection = sortDirection;
        }

        Sort sort = null;

        if(querySortDirection.equalsIgnoreCase(Sort.Direction.DESC.name())) {
            sort = Sort.by(querySortField).descending();
        }

        if(querySortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())) {
            sort = Sort.by(querySortField).ascending();
        }

        if(sort == null) {
            return PageRequest.of(queryPageNumber, queryPageSize);
        }

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }
    public static PageDtoResponse generatePageDtoResponse(
            List<?> contents, int pageNumber, int pageSize, long totalElements, int totalPages, boolean last) {
        return new PageDtoResponse(contents, pageNumber, pageSize, totalElements, totalPages, last);
    }
}
