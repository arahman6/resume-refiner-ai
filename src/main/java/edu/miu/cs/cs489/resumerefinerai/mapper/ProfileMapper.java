package edu.miu.cs.cs489.resumerefinerai.mapper;

import edu.miu.cs.cs489.resumerefinerai.dto.request.CreateProfileRequest;
import edu.miu.cs.cs489.resumerefinerai.dto.response.ProfileResponse;
import edu.miu.cs.cs489.resumerefinerai.model.Profile;
import edu.miu.cs.cs489.resumerefinerai.auth.user.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    Profile toEntity(CreateProfileRequest dto, User user);

    @Mapping(target = "username", source = "user.username")
    ProfileResponse toDto(Profile profile);
}
