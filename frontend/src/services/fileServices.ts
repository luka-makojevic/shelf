import { AxiosRequestConfig } from 'axios';
import { API_URL_FILESYSTEM } from '../api/api';
import instance from '../api/axiosInstance';

const API_URL_FILES = 'http://10.10.0.136:8080/filesystem/';

const getShelfFiles = (shelfId: number) =>
  instance.get(`${API_URL_FILES}shelf/${shelfId}`);

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

const editFile = (data: { fileId: number; fileName: string }) =>
  instance.put(`${API_URL_FILES}file/rename`, data);

const editFolder = (data: { folderId: number; folderName: string }) =>
  instance.put(`${API_URL_FILES}folder/rename`, data);
  
const downloadFile = (fileId: number) =>
  instance({
    url: `${API_URL_FILES}file/download/${fileId}?file=true`,
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
  editFile,
  editFolder,
  downloadFile,
  uploadFiles,
};
