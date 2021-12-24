import { API_URL_FILESYSTEM } from '../api/api';
import instance from '../api/axiosInstance';

const getShelfFiles = (shelfId: number) =>
  instance.get(`${API_URL_FILESYSTEM}shelf/${shelfId}`);

const getFolderFiles = (folderId: number) =>
  instance.get(`${API_URL_FILESYSTEM}folder/${folderId}`);

const createFolder = (
  folderName: string,
  shelfId: number,
  parentFolderId = 0
) =>
  instance.post(`${API_URL_FILESYSTEM}folder`, {
    folderName,
    shelfId,
    parentFolderId,
  });

const softDeleteFolder = (folderIds: number[]) =>
  instance.put(`${API_URL_FILESYSTEM}folder/move-to-trash`, folderIds);

const softDeleteFile = (fileIds: number[]) =>
  instance.put(`${API_URL_FILESYSTEM}file/move-to-trash`, fileIds);

const hardDeleteFile = (data: number[]) =>
  instance.delete(`${API_URL_FILESYSTEM}file`, { data });

const hardDeleteFolder = (data: number[]) =>
  instance.delete(`${API_URL_FILESYSTEM}folder`, { data });

const recoverFileFromTrash = (data: number) =>
  instance.put(`${API_URL_FILESYSTEM}trash/file`, data);

const recoverFolderFromTrash = (data: number) =>
  instance.put(`${API_URL_FILESYSTEM}trash/file`, data);

export default {
  getShelfFiles,
  getFolderFiles,
  createFolder,
  softDeleteFolder,
  softDeleteFile,
  hardDeleteFile,
  hardDeleteFolder,
  recoverFileFromTrash,
  recoverFolderFromTrash,
};
