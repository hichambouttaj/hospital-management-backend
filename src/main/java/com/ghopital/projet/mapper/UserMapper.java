package com.ghopital.projet.mapper;

import com.ghopital.projet.dto.request.UserDtoRequest;
import com.ghopital.projet.dto.response.UserDtoResponse;
import com.ghopital.projet.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    User userDtoRequestToUser(UserDtoRequest userDtoRequest);
    @Mapping(target = "imageId", source = "user.image.id")
    UserDtoResponse userToUserDtoResponse(User user);
}
