package com.swapll.gradu.model.dto.mappers;

import com.swapll.gradu.model.User;
import com.swapll.gradu.model.dto.UserDTO;

import java.util.Base64;

public class UserMapper {

    public static User toEntity(UserDTO dto) {
        User user = new User();
        user.setUserName(dto.getUserName());
        user.setBio(dto.getBio());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setReferralCode(dto.getReferralCode());
        user.setPassword(dto.getPassword());

//        if (dto.getProfilePic() != null) {
//            user.setProfilePic(Base64.getDecoder().decode(dto.getProfilePic()));
//        }

        return user;
    }

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setReferralCode(user.getReferralCode());
        dto.setMyReferralCode(user.getMyReferralCode());
        dto.setBio(user.getBio());

        if (user.getId() != null && user.getProfilePic() != null) {
            dto.setProfilePic("/api/users/" + user.getId() + "/profile-pic");

        }
        else{
            dto.setProfilePic(null);
        }

        return dto;
    }
}
