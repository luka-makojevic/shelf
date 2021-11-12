package com.htec.shelfserver.mapper;

import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.requestModel.UserRequestModel;
import com.htec.shelfserver.responseModel.UserResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.htec.shelfserver.entity.UserEntity;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO userRequestModelToUserDto(UserRequestModel userRequestModel);

    UserEntity userDtoToUser(UserDTO userDTO);

    UserDTO userToUserDTO(UserEntity user);

    UserResponseModel userDtoToUserResponseModel(UserDTO userDTO);

}
