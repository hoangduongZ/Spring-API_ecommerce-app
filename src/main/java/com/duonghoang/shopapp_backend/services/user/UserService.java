package com.duonghoang.shopapp_backend.services.user;

import com.duonghoang.shopapp_backend.dtos.UserDTO;
import com.duonghoang.shopapp_backend.exception.DataNotFoundException;
import com.duonghoang.shopapp_backend.models.Role;
import com.duonghoang.shopapp_backend.models.User;
import com.duonghoang.shopapp_backend.repositories.RoleRepository;
import com.duonghoang.shopapp_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public User createUser(UserDTO userDTO) {
        String phoneNumber = userDTO.getPhoneNumber();
        if (!userRepository.existsByPhoneNumber(phoneNumber)){
            throw new RuntimeException("User is not existed !");
        }

        if (!userDTO.getRetypePassword().equals(userDTO.getPassword())){
            throw new RuntimeException("Retype password does not match !");
        }
        User newUser = User.builder().fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFaceBookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();

        Role existingRole = roleRepository.findById(userDTO.getRoleId()).orElseThrow(
                ()->new DataNotFoundException("Role not found"));
        newUser.setRole(existingRole);

        if (userDTO.getGoogleAccountId()!= null || userDTO.getFaceBookAccountId() !=null){
            String password = userDTO.getPassword();
        }

        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) {

        return "";
    }
}
