<<<<<<< HEAD:backend/account/src/main/java/com/htec/account/mapper/UserMapper.java
package com.htec.account.mapper;

import com.htec.account.dto.AuthUser;
import com.htec.account.dto.UserDTO;
import com.htec.account.entity.UserEntity;
import com.htec.account.model.request.UserLoginMicrosoftRequestModel;
import com.htec.account.model.request.UserLoginRequestModel;
import com.htec.account.model.request.UserRegisterRequestModel;
import com.htec.account.model.request.UserUpdateRequestModel;
import com.htec.account.model.response.RefreshTokenResponseModel;
import com.htec.account.model.response.UserLoginResponseModel;
import com.htec.account.model.response.UserRegisterMicrosoftResponseModel;
import com.htec.account.model.response.UserResponseModel;
package com.htec.shelfserver.mapper;

import com.htec.shelfserver.dto.AuthUser;
import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.model.request.*;
import com.htec.shelfserver.model.response.RefreshTokenResponseModel;
import com.htec.shelfserver.model.response.UserLoginResponseModel;
import com.htec.shelfserver.model.response.UserRegisterMicrosoftResponseModel;
import com.htec.shelfserver.model.response.UserResponseModel;
>>>>>>> CU-1v6g6ne - Upload and set profile picture:backend/shelf-server/src/main/java/com/htec/shelfserver/mapper/UserMapper.java
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO userRegisterRequestModelToUserDto(UserRegisterRequestModel userRegisterRequestModel);

    UserEntity userDtoToUserEntity(UserDTO userDTO);

    UserDTO userEntityToUserDTO(UserEntity user);

    UserDTO authUserToUserDTO(AuthUser authUser);

    UserResponseModel userDtoToUserResponseModel(UserDTO userDTO);

    RefreshTokenResponseModel userDtoToRefreshTokenResponseModel(RefreshTokenResponseModel refreshTokenResponseModel);

    UserResponseModel userEntityToUserResponseModel(UserEntity userEntity);

    List<UserResponseModel> userEntityToUserResponseModels(List<UserEntity> userEntities);

    UserDTO userLoginRequestModelToUserDto(UserLoginRequestModel userLoginRequestModel);

    UserDTO userLoginMicrosoftRequestModelToUserDto(UserLoginMicrosoftRequestModel userLoginMicrosoftRequestModel);

    @Mapping(target = "role", source = "userDTO.role.id")
    UserLoginResponseModel userDtoToUserLoginResponseModel(UserDTO userDTO, String jwtToken);

    @Mapping(target = "firstName", source = "givenName")
    @Mapping(target = "lastName", source = "surname")
    @Mapping(target = "email", source = "mail")
    UserEntity userRegisterMicrosoftResponseModelToUserEntity(UserRegisterMicrosoftResponseModel userRegisterMicrosoftResponseModel);

    UserDTO userUpdateRequestModelToUserDto(UserUpdateRequestModel userUpdateRequestModel);

    UserDTO updateUserPhotoByIdToUserDto(UpdateUserPhotoByIdRequestModel updateUserPhotoByIdRequestModel);

    @Mapping(target = "role", source = "userDTO.role.id")
    UserLoginResponseModel userDtoToUserLoginResponseModel(UserDTO userDTO, String jwtToken, String jwtRefreshToken);

}
