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
    private Integer id;
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phone;
    private String address;
    private String referralCode;
    private String myReferralCode;
    private String bio;
    private String profilePic; // This will be a URL (not the image bytes)


    public UserDTO(Integer id,String userName, String firstName, String lastName, String email, String phone, String address,String bio, String profilePic, String referralCode,String myReferralCode) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.profilePic = profilePic;
        this.referralCode = referralCode;
        this.myReferralCode=myReferralCode;
        this.bio=bio;
        this.id=id;
    }

}
