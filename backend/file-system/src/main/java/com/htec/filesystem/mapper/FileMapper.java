package com.htec.filesystem.mapper;

import com.htec.filesystem.dto.ShelfDTO;
import com.htec.filesystem.entity.ShelfEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FileMapper {

    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    ShelfDTO shelfEntityToShelfDto(ShelfEntity shelfEntity);
}
