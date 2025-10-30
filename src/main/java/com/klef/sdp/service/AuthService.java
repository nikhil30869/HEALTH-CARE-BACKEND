package com.klef.sdp.service;

public interface AuthService {
    AuthResponse login(String username, String password, String role);

    class AuthResponse {
        private String role;
        private Object userData;

        public AuthResponse(String role, Object userData) {
            this.role = role;
            this.userData = userData;
        }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public Object getUserData() { return userData; }
        public void setUserData(Object userData) { this.userData = userData; }
    }
}
