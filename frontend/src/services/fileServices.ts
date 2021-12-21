import instance from '../api/axiosInstance';

const API_URL_FILES = 'http://10.10.0.136:8080/filesystem/';

const getShelfFiles = (shlefId: number) =>
  instance.get(`${API_URL_FILES}shelf/${shlefId}`);

const getFolderFiles = (folderId: number) =>
  instance.get(`${API_URL_FILES}folder/${folderId}`);

const createFolder = (
  folderName: string,
  shelfId: number,
  parentFolderId = 0
) =>
  instance.post(`${API_URL_FILES}folder`, {
    folderName,
    shelfId,
    parentFolderId,
  });

const softDeleteFolder = (folderIds: number[]) =>
  instance.put(`${API_URL_FILES}folder/move-to-trash`, folderIds);

const softDeleteFile = (fileIds: number[]) =>
  instance.put(`${API_URL_FILES}file/move-to-trash`, fileIds);

export default {
  getShelfFiles,
  getFolderFiles,
  createFolder,
  softDeleteFolder,
  softDeleteFile,
};
