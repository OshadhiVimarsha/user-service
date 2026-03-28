package lk.ijse.eca.userservice.mapper;

import lk.ijse.eca.userservice.dto.UserRequestDTO;
import lk.ijse.eca.userservice.dto.UserResponseDTO;
import lk.ijse.eca.userservice.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

    UserResponseDTO toResponseDto(User user);

    User toEntity(UserRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "nic", ignore = true)
    void updateEntity(UserRequestDTO dto, @MappingTarget User user);
}