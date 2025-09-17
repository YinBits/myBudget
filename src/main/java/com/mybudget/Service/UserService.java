package com.mybudget.Service;

import com.mybudget.DTOs.CreateUserDto;
import com.mybudget.DTOs.LoginUserDto;
import com.mybudget.DTOs.RecoveryJwtTokenDto;
import com.mybudget.Entity.Role;
import com.mybudget.Entity.User;
import com.mybudget.Repository.UserRepository;
import com.mybudget.Security.SecurityConfiguration;
import com.mybudget.Security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;


    @Service
    public class UserService {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private JwtTokenService jwtTokenService;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private SecurityConfiguration securityConfiguration;

        public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password());
            
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
        }
        
        
        public void createUser(CreateUserDto createUserDto) {
            User newUser = User.builder()
                    .name(createUserDto.name())
                    .email(createUserDto.email())
                    .password(securityConfiguration.passwordEncoder().encode(createUserDto.password()))
                    .roles(List.of(Role.builder().name(createUserDto.role()).build()))
                    .build();

       
            userRepository.save(newUser);
        }
    }
