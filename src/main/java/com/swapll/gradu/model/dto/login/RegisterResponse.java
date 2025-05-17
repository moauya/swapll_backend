package com.swapll.gradu.model.dto.login;

import com.swapll.gradu.model.dto.UserDTO;

public class RegisterResponse {
    private String token;
    private UserDTO user;

    public RegisterResponse() {

    }

    public RegisterResponse(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
