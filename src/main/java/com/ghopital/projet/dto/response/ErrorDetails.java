package com.ghopital.projet.dto.response;


import java.util.Date;

public record ErrorDetails(
        Date timestamp,
        int status,
        String error,
        String message,
        String details
) {
}
