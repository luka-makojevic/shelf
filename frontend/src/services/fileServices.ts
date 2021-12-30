import { AxiosRequestConfig } from 'axios';
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

const recoverFileFromTrash = (data: number[]) =>
  instance.put(`${API_URL_FILESYSTEM}file/recover`, data);

const recoverFolderFromTrash = (data: number[]) =>
  instance.put(`${API_URL_FILESYSTEM}folder/recover`, data);

const editFile = (data: { fileId: number; fileName: string }) =>
  instance.put(`${API_URL_FILESYSTEM}file/rename`, data);

const editFolder = (data: { folderId: number; folderName: string }) =>
  instance.put(`${API_URL_FILESYSTEM}folder/rename`, data);

const downloadFile = (fileId: number) =>
  instance({
    url: `${API_URL_FILESYSTEM}file/download/${fileId}?file=true`,
    method: 'GET',
    responseType: 'blob',
  });

const uploadFiles = (
  shelfId: number,
  folderId: number,
  files: FormData,
  options: AxiosRequestConfig
) =>
  instance.post(
    `${API_URL_FILESYSTEM}file/upload/${shelfId}/${folderId}`,
    files,
    options
  );

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
  editFile,
  editFolder,
  downloadFile,
  uploadFiles,
};
