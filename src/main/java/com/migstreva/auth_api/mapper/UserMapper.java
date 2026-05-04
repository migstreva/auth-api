package com.migstreva.auth_api.mapper;

import com.migstreva.auth_api.dto.UserRegisterDTO;
import com.migstreva.auth_api.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRegisterDTO dto);
}
