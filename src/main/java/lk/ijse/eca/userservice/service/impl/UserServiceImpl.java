package lk.ijse.eca.userservice.service.impl;

import lk.ijse.eca.userservice.dto.UserRequestDTO;
import lk.ijse.eca.userservice.dto.UserResponseDTO;
import lk.ijse.eca.userservice.entity.User;
import lk.ijse.eca.userservice.exception.DuplicateUserException;
import lk.ijse.eca.userservice.exception.UserNotFoundException;
import lk.ijse.eca.userservice.mapper.UserMapper;
import lk.ijse.eca.userservice.repository.UserRepository;
import lk.ijse.eca.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto) {
        log.debug("Creating user with NIC: {}", dto.getNic());

        if (userRepository.existsById(dto.getNic())) {
            log.warn("Duplicate NIC detected: {}", dto.getNic());
            throw new DuplicateUserException(dto.getNic());
        }

        User user = userMapper.toEntity(dto);
        userRepository.save(user);

        log.info("User created successfully: {}", dto.getNic());
        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(String nic, UserRequestDTO dto) {
        log.debug("Updating user with NIC: {}", nic);

        User user = userRepository.findById(nic)
                .orElseThrow(() -> {
                    log.warn("User not found for update: {}", nic);
                    return new UserNotFoundException(nic);
                });

        userMapper.updateEntity(dto, user);
        userRepository.save(user);

        log.info("User updated successfully: {}", nic);
        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(String nic) {
        log.debug("Deleting user with NIC: {}", nic);

        User user = userRepository.findById(nic)
                .orElseThrow(() -> {
                    log.warn("User not found for deletion: {}", nic);
                    return new UserNotFoundException(nic);
                });

        userRepository.delete(user);

        log.info("User deleted successfully: {}", nic);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUser(String nic) {
        log.debug("Fetching user with NIC: {}", nic);

        return userRepository.findById(nic)
                .map(userMapper::toResponseDto)
                .orElseThrow(() -> {
                    log.warn("User not found: {}", nic);
                    return new UserNotFoundException(nic);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        log.debug("Fetching all users");

        List<UserResponseDTO> users = userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());

        log.debug("Fetched {} users", users.size());
        return users;
    }
}
