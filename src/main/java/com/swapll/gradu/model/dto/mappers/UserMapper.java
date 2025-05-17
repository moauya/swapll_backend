package com.swapll.gradu.model.dto.mappers;

import com.swapll.gradu.model.User;
import com.swapll.gradu.model.dto.UserDTO;

public class UserMapper {


    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getUserName(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getProfilePic(),
                user.getReferralCode()
        );
    }



    public static User toEntity(UserDTO userDTO) {
        User user = new User();

        user.setUserName(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setProfilePic(userDTO.getProfilePic());
        user.setReferralCode(userDTO.getReferralCode());
        return user;
    }
}
