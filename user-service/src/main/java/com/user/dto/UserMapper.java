package com.user.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.user.entity.UserEntity;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {

   
    @Mapping(target = "password", ignore = true)
    User toDomain(UserEntity entity);

    UserEntity toEntity(User user);

     
    @Mapping(target = "password", ignore = true)
    List<User> toDomainList(List<UserEntity> entities);


}