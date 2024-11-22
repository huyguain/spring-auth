package com.example.spring_auth.services.user;

import com.example.spring_auth.components.LocalizationUtils;
import com.example.spring_auth.dtos.UserDTO;
import com.example.spring_auth.exceptions.DataNotFoundException;
import com.example.spring_auth.models.Role;
import com.example.spring_auth.models.User;
import com.example.spring_auth.repositories.RoleRepository;
import com.example.spring_auth.repositories.UserRepository;
import com.example.spring_auth.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LocalizationUtils localizationUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User createUser(UserDTO userDTO) throws Exception {
        String phoneNumber = userDTO.getPhoneNumber();
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Số điện thoại đã tồn tại");
        }
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException(
                        localizationUtils.getLocalizedMessage(MessageKeys.ROLE_DOES_NOT_EXISTS)));
        if (role.getName().toUpperCase().equals(Role.ADMIN)) {
            throw new Exception("Không được phép đăng ký tài khoản Admin");
        }
        User newUser = User.builder()
                .fullName(userDTO.getFullname())
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .active(true)
                .build();
        newUser.setRole(role);

        if (userDTO.getGoogleAccountId() == 0 && userDTO.getFacebookAccountId() == 0) {
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }
}
