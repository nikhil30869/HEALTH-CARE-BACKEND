package com.klef.sdp.dto;

public class ApiResponseDTO {
    private boolean success;
    private String message;
    private Object data;

    public ApiResponseDTO() {}

    public ApiResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResponseDTO(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    // Static helper methods
    public static ApiResponseDTO success(String message) {
        return new ApiResponseDTO(true, message);
    }

    public static ApiResponseDTO success(String message, Object data) {
        return new ApiResponseDTO(true, message, data);
    }

    public static ApiResponseDTO error(String message) {
        return new ApiResponseDTO(false, message);
    }
}