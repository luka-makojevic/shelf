package com.htec.filesystem.mapper;

import com.htec.filesystem.dto.BreadCrumbDTO;
import com.htec.filesystem.entity.FolderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BreadCrumbsMapper {

    BreadCrumbsMapper INSTANCE = Mappers.getMapper(BreadCrumbsMapper.class);

    @Mapping(target = "folderName", source = "name")
    @Mapping(target = "folderId", source = "id")
    BreadCrumbDTO folderEntityToBreadCrumbDTO(FolderEntity folderEntity);

    List<BreadCrumbDTO> folderEntitiesToBreadCrumbDTOs(List<FolderEntity> folderEntities);
}
