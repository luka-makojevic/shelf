package com.htec.filesystem.mapper;

import com.htec.filesystem.dto.FileDTO;
import com.htec.filesystem.dto.ShelfDTO;
import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.entity.ShelfEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FileMapper {

    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    ShelfDTO shelfEntityToShelfDto(ShelfEntity shelfEntity);

    @Mapping(target = "folder", constant = "false")
    FileDTO fileEntityToFileDTO(FileEntity fileEntity);

    @Mapping(target = "folder", constant = "true")
    FileDTO fileEntityToFileDTO(FolderEntity folderEntity);

    List<FileDTO> fileEntityToFileDTO(List<FileEntity> fileEntities);

    List<FileDTO> folderEntityToFileDTO(List<FolderEntity> folderEntities);
}
