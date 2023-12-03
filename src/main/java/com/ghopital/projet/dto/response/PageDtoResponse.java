package com.ghopital.projet.dto.response;

import java.util.List;

public record PageDtoResponse(
        List<?> contents,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last
) {
}
