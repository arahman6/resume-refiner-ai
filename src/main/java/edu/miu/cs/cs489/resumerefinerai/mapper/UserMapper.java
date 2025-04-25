package edu.miu.cs.cs489.resumerefinerai.mapper;

import edu.miu.cs.cs489.resumerefinerai.dto.response.UserResponse;
import edu.miu.cs.cs489.resumerefinerai.auth.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toDto(User user);
}
