package com.htec.filesystem.mapper;

import com.htec.filesystem.dto.ShelfItemDTO;
import com.htec.filesystem.entity.FileDeletedEntity;
import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderDeletedEntity;
import com.htec.filesystem.entity.FolderEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface EntitiesMapper {

    FolderEntity folderDeletedEntityToFolderEntity(FolderDeletedEntity folderDeletedEntity);

    List<FolderEntity> folderDeletedEntitiesToFolderEntities(List<FolderDeletedEntity> folderDeletedEntities);

    FileEntity fileDeletedEntityToFileEntity(FileDeletedEntity fileDeletedEntity);

    List<FileEntity> fileDeletedEntitiesToFileEntities(List<FileDeletedEntity> fileDeletedEntities);

    FolderDeletedEntity folderEntityToFolderDeletedEntity(FolderEntity fod);

    List<FolderDeletedEntity> folderEntitiesToFolderDeletedEntities(List<FolderEntity> folderEntities);

    FolderDeletedEntity fileEntityToFileDeletedEntity(FolderDeletedEntity fileDeletedEntity);

    List<FileDeletedEntity> fileEntitiesToFileDeletedEntities(List<FileEntity> fileEntities);
}
