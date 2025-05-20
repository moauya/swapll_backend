package com.swapll.gradu.controller;

import com.swapll.gradu.model.User;
import com.swapll.gradu.model.dto.*;
import com.swapll.gradu.model.dto.login.LoginRequest;
import com.swapll.gradu.model.dto.login.LoginResponse;
import com.swapll.gradu.model.dto.login.RegisterResponse;
import com.swapll.gradu.model.dto.mappers.UserMapper;
import com.swapll.gradu.security.CustomUserDetails;
import com.swapll.gradu.security.JwtUtil;
import com.swapll.gradu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestPart("user") UserDTO userDTO,
                                                     @RequestPart(value = "profilePic", required = false) MultipartFile profilePic) {
        UserDTO registeredUser = userService.registerUser(userDTO, profilePic);
        String token = jwtUtil.generateToken(new CustomUserDetails(UserMapper.toEntity(registeredUser)));
        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponse(token, registeredUser));
    }



    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        System.out.println("Attempting to authenticate user: " + request.getUsernameOrEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(), request.getPassword()));
        } catch (BadCredentialsException e) {
            System.out.println("Authentication failed for: " + request.getUsernameOrEmail());
            throw new UsernameNotFoundException("Invalid credentials");
        }

        User user = userService.getUserByEmailOrUsername(request.getUsernameOrEmail());

        String jwt = jwtUtil.generateToken(new CustomUserDetails(user));
        System.out.println("Generated JWT: " + jwt);

        return ResponseEntity.ok(new LoginResponse(jwt));
    }


}
