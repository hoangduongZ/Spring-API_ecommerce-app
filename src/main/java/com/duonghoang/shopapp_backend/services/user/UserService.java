package com.duonghoang.shopapp_backend.services.user;

import com.duonghoang.shopapp_backend.component.JwtTokenUtil;
import com.duonghoang.shopapp_backend.dtos.UserDTO;
import com.duonghoang.shopapp_backend.exception.DataNotFoundException;
import com.duonghoang.shopapp_backend.exception.PermissionDenyException;
import com.duonghoang.shopapp_backend.models.Role;
import com.duonghoang.shopapp_backend.models.User;
import com.duonghoang.shopapp_backend.repositories.RoleRepository;
import com.duonghoang.shopapp_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public User createUser(UserDTO userDTO) throws Exception {
//        Register user
        String phoneNumber = userDTO.getPhoneNumber();
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new RuntimeException("User is existed !");
        }

        if (!userDTO.getRetypePassword().equals(userDTO.getPassword())) {
            throw new RuntimeException("Retype password does not match !");
        }

        Role existingRole = roleRepository.findById(userDTO.getRoleId()).orElseThrow(
                () -> new DataNotFoundException("Role not found"));

        if (existingRole.getName().equalsIgnoreCase("ADMIN")) {
            throw new PermissionDenyException("You can not register an admin account");
        }
        User newUser = User.builder().fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFaceBookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();
        newUser.setRole(existingRole);
        if (userDTO.getGoogleAccountId().isEmpty() && userDTO.getFaceBookAccountId().isEmpty()) {
            String password = userDTO.getPassword();
            String encodePassword = passwordEncoder.encode(password);
            newUser.setPassword(encodePassword);
        }

        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password, long roleId) throws Exception {
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new UsernameNotFoundException("Invalid username or password"));

//        Check password
        if (user.getGoogleAccountId().isEmpty() && user.getFacebookAccountId().isEmpty()) {
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("Invalid user name or password");
            }
        }
        if (!user.isActive()) {
            throw new DataNotFoundException("User is locked!");
        }

//        Check role
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new DataNotFoundException("Role not existed"));
        if (user.getRole().getId() != roleId) {
            throw new BadCredentialsException("Invalid role");
        }

//        Authenticate
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password, user.getAuthorities()
        );
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(user);
    }

    @Override
    public User getUserDetailsFromToken(String token) throws Exception {
        if (jwtTokenUtil.isTokenExpired(token)) {
            throw new Exception("Token expired!");
        }

        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new DataNotFoundException("User not found");
        }
    }
}
