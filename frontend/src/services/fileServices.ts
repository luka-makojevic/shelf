import { AxiosRequestConfig } from 'axios';
import instance from '../api/axiosInstance';
import { TableDataTypes } from '../interfaces/dataTypes';

const API_URL_FILES = '/filesystem';

const getShelfFiles = (shelfId: number) =>
  instance.get(`${API_URL_FILES}/shelf/${shelfId}`);

const softDeleteFile = (fileIds: number[]) =>
  instance.put(`${API_URL_FILES}/file/move-to-trash`, fileIds);

const hardDeleteFile = (data: number[]) =>
  instance.delete(`${API_URL_FILES}/file`, { data });

const recoverFileFromTrash = (data: number[]) =>
  instance.put(`${API_URL_FILES}/file/recover`, data);

const editFile = (data: { fileId: number; fileName: string }) =>
  instance.put(`${API_URL_FILES}/file/rename`, data);

const downloadSingleFile = (fileId: any) =>
  instance({
    url: `${API_URL_FILES}/file/download/${fileId}`,
    method: 'GET',
    responseType: 'blob',
  });
const downloadFiles = (fileIds: any) =>
  instance({
    url: `${API_URL_FILES}/file/zip-download?fileIds=${fileIds}`,

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
    `${API_URL_FILES}/file/upload/${shelfId}/${folderId}`,
    files,
    options
  );

const getProfilePicture = (userId?: number) =>
  `${process.env.REACT_APP_API_URL}${API_URL_FILES}/file/preview/${userId}?file=false`;

const uploadProfilePicture = (
  id: number,
  data: FormData,
  options: AxiosRequestConfig
) => instance.post(`${API_URL_FILES}/file/upload/profile/${id}`, data, options);

export default {
  getShelfFiles,
  softDeleteFile,
  hardDeleteFile,
  recoverFileFromTrash,
  editFile,
  downloadFiles,
  downloadSingleFile,
  uploadFiles,
  getProfilePicture,
  uploadProfilePicture,
};
