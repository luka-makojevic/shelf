package com.htec.shelffunction.mapper;

import com.htec.shelffunction.dto.FunctionDTO;
import com.htec.shelffunction.entity.FunctionEntity;
import com.htec.shelffunction.model.request.CustomFunctionRequestModel;
import com.htec.shelffunction.model.request.PredefinedFunctionRequestModel;
import com.htec.shelffunction.model.response.FunctionResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FunctionMapper {

    FunctionMapper INSTANCE = Mappers.getMapper(FunctionMapper.class);

    @Mapping(target = "custom", constant = "false")
    @Mapping(target="event", expression="java(FunctionEntity.createEvent(functionRequestModel.getEventId()))")
    FunctionEntity predefinedFunctionRequestModelToFunctionEntity(PredefinedFunctionRequestModel functionRequestModel);

    @Mapping(target="event", expression="java(FunctionEntity.createEvent(customFunctionRequestModel.getEventId()))")
    FunctionEntity customFunctionRequestModelToFunctionEntity(CustomFunctionRequestModel customFunctionRequestModel);

    @Mapping(target = "eventId", source = "functionEntity.event.id")
    FunctionDTO functionEntityToFunctionDto(FunctionEntity functionEntity);

    List<FunctionDTO> functionEntitiesToFunctionDtos(List<FunctionEntity> functionEntities);

    @Mapping(target = "functionId", source = "id")
    @Mapping(target = "functionName", source = "name")
    @Mapping(target="eventId", source = "functionEntity.event.id")
    FunctionResponseModel functionEntityToFunctionResponseModel(FunctionEntity functionEntity);
}


