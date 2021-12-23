package com.htec.filesystem.mapper;

import com.htec.filesystem.dto.ShelfDTO;
import com.htec.filesystem.dto.ShelfItemDTO;
import com.htec.filesystem.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ShelfItemMapper {

    ShelfItemMapper INSTANCE = Mappers.getMapper(ShelfItemMapper.class);

    ShelfDTO shelfEntityToShelfDto(ShelfEntity shelfEntity);

    List<ShelfDTO> shelfEntitiesToShelfDTOs(List<ShelfEntity> shelfEntities);

    @Mapping(target = "folder", constant = "false")
    ShelfItemDTO fileEntityToShelfItemDTO(FileEntity fileEntity);

    @Mapping(target = "folder", constant = "true")
    ShelfItemDTO folderEntityToShelfItemDTO(FolderEntity folderEntity);

    List<ShelfItemDTO> fileEntitiesToShelfItemDTOs(List<FileEntity> fileEntities);

    List<ShelfItemDTO> folderEntitiesToShelfItemDTOs(List<FolderEntity> folderEntities);

    ShelfItemDTO fileDeletedEntityToShelfItemDTO(FileDeletedEntity fileDeletedEntity);

    List<ShelfItemDTO> fileDeletedEntitiesToShelfItemDTOs(List<FileDeletedEntity> fileDeletedEntities);

    ShelfItemDTO folderDeletedEntityToShelfItemDTO(FolderDeletedEntity folderDeletedEntity);

    List<ShelfItemDTO> folderDeletedEntitiesToShelfItemDTOs(List<FolderDeletedEntity> folderDeletedEntities);
}
