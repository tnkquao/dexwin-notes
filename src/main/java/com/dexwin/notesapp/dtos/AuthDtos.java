package com.dexwin.notesapp.dtos;

import lombok.Data;

public class AuthDtos {

    @Data
    public static class SignupRequest {
        private String username;
        private String password;
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class JwtResponse {
        private String token;
        private String username;

        public JwtResponse(String token, String username) {
            this.token = token;
            this.username = username;
        }
    }
}