package com.dexwin.notesapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDTO {
    private boolean success;

    private Object data;

    private ApiError error;
}
