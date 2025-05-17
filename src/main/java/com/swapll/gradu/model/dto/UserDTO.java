package com.swapll.gradu.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phone;
    private String address;
    private String profilePic;
    private String referralCode;

    public UserDTO(String userName, String firstName, String lastName, String email, String phone, String address, String profilePic, String referralCode) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.profilePic = profilePic;
        this.referralCode = referralCode;
    }

}
