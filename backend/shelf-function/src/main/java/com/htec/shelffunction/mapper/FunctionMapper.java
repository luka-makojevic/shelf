package com.htec.shelffunction.mapper;

import com.htec.shelffunction.entity.FunctionEntity;
import com.htec.shelffunction.model.request.PredefinedFunctionRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FunctionMapper {

    FunctionMapper INSTANCE = Mappers.getMapper(FunctionMapper.class);

    @Mapping(target = "custom", constant = "false")
    @Mapping(target="event", expression="java(FunctionEntity.createEvent(functionRequestModel.getEventId()))")
    FunctionEntity predefinedFunctionRequestModelToFunctionEntity(PredefinedFunctionRequestModel functionRequestModel);
}


