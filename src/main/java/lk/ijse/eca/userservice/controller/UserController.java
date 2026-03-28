package lk.ijse.eca.userservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import lk.ijse.eca.userservice.dto.UserRequestDTO;
import lk.ijse.eca.userservice.dto.UserResponseDTO;
import lk.ijse.eca.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserService userService;

    private static final String NIC_REGEXP = "^\\d{9}[vV]$";

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDTO> createUser(
            @Validated({Default.class, UserRequestDTO.OnCreate.class}) @ModelAttribute UserRequestDTO dto) {
        log.info("POST /api/v1/users - NIC: {}", dto.getNic());
        UserResponseDTO response = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/{nic}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable @Pattern(regexp = NIC_REGEXP, message = "NIC must be 9 digits followed by V or v") String nic,
            @Valid @ModelAttribute UserRequestDTO dto) {
        log.info("PUT /api/v1/users/{}", nic);
        UserResponseDTO response = userService.updateUser(nic, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{nic}")
    public ResponseEntity<Void> deleteUser(@PathVariable String nic) {
        log.info("DELETE /api/v1/users/{}", nic);

        // Service layer call
        userService.deleteUser(nic); // NIC අනුව DB record remove කරයි

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{nic}")
    public ResponseEntity<UserResponseDTO> getUser(
            @PathVariable @Pattern(regexp = NIC_REGEXP, message = "NIC must be 9 digits followed by V or v") String nic) {
        log.info("GET /api/v1/users/{}", nic);
        UserResponseDTO response = userService.getUser(nic);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUser() {
        log.info("GET /api/v1/users");
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
