package com.swapll.gradu.service;

import com.swapll.gradu.model.User;
import com.swapll.gradu.model.dto.UserDTO;
import com.swapll.gradu.model.dto.mappers.UserMapper;
import com.swapll.gradu.repository.UserRepository;
import com.swapll.gradu.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.Base64;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register user and return the DTO
    public UserDTO registerUser(UserDTO userDTO, MultipartFile profilePic) {
        if (userRepository.existsByUserNameIgnoreCase(userDTO.getUserName())) {
            throw new IllegalArgumentException("Username already taken");
        }

        if (userRepository.existsByEmailIgnoreCase(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = UserMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        if (profilePic != null && !profilePic.isEmpty()) {
            try {
                user.setProfilePic(profilePic.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to process profile picture", e);
            }
        }

        if (userDTO.getReferralCode() != null &&
                userRepository.existsByMyReferralCodeIgnoreCase(userDTO.getReferralCode())) {

            user.setBalance(user.getBalance() + 10);
            User referrer = userRepository.findByMyReferralCode(userDTO.getReferralCode())
                    .orElseThrow(() -> new RuntimeException("Referral code not found"));
            referrer.setBalance(referrer.getBalance() + 3);
            userRepository.save(referrer);
        } else {
            user.setReferralCode(null);
        }

        userRepository.save(user);
        return UserMapper.toDTO(user);
    }


    @Transactional
    public UserDTO updateUser(UserDTO updatedUserDTO, MultipartFile profilePic) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User owner = userDetails.getUser();

        if (updatedUserDTO.getUserName() != null &&
                !updatedUserDTO.getUserName().equals(owner.getUserName())) {
            if (userRepository.existsByUserNameIgnoreCase(updatedUserDTO.getUserName())) {
                throw new IllegalArgumentException("Username already taken");
            }
            owner.setUserName(updatedUserDTO.getUserName());
        }

        if (updatedUserDTO.getEmail() != null &&
                !updatedUserDTO.getEmail().equals(owner.getEmail())) {
            if (userRepository.existsByEmailIgnoreCase(updatedUserDTO.getEmail())) {
                throw new IllegalArgumentException("Email already in use");
            }
            owner.setEmail(updatedUserDTO.getEmail());
        }

        if (updatedUserDTO.getFirstName() != null) owner.setFirstName(updatedUserDTO.getFirstName());
        if (updatedUserDTO.getLastName() != null) owner.setLastName(updatedUserDTO.getLastName());
        if (updatedUserDTO.getPhone() != null) owner.setPhone(updatedUserDTO.getPhone());
        if (updatedUserDTO.getAddress() != null) owner.setAddress(updatedUserDTO.getAddress());
        if (updatedUserDTO.getReferralCode() != null) owner.setReferralCode(updatedUserDTO.getReferralCode());

        if (profilePic != null && !profilePic.isEmpty()) {
            try {
                owner.setProfilePic(profilePic.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to update profile picture", e);
            }
        }

        User updatedOwner = userRepository.save(owner);
        return UserMapper.toDTO(updatedOwner);
    }


    public User getUserByEmailOrUsername(String username) {
        return userRepository.findByUserNameIgnoreCaseOrEmailIgnoreCase(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));
    }

    public UserDTO getUserById(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        return UserMapper.toDTO(user);
    }

    @Transactional
    public void changePassword(String oldPassword, String newPassword) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User user = userDetails.getUser();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public UserDTO getUserInfo(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User owner = userDetails.getUser();
        UserDTO dto=UserMapper.toDTO(owner);

        return dto;
    }
}
