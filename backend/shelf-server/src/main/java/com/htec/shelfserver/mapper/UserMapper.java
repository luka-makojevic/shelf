package com.htec.shelfserver.mapper;

import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.model.request.UserRegisterMicrosoftRequestModel;
import com.htec.shelfserver.model.request.UserRegisterRequestModel;
import com.htec.shelfserver.model.response.UserResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO userRegisterRequestModelToUserDto(UserRegisterRequestModel userRegisterRequestModel);
<<<<<<< HEAD
=======

    UserDTO userRegisterMicrosoftRequestModelToUserDto(UserRegisterMicrosoftRequestModel userRegisterRequestModel);
>>>>>>> CU-1rb0ck7 - Third party registration , new request-model added

    UserEntity userDtoToUserEntity(UserDTO userDTO);

    UserDTO userEntityToUserDTO(UserEntity user);

    UserResponseModel userDtoToUserResponseModel(UserDTO userDTO);

    UserResponseModel userEntityToUserResponseModel(UserEntity userEntity);

    List<UserResponseModel> userEntityToUserResponseModels(List<UserEntity> userEntities);

}
