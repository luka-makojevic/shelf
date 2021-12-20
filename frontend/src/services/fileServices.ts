import instance from '../api/axiosInstance';

const API_URL_FILES = 'http://10.10.0.136:8080/filesystem/';

const getShelfFiles = (shlefId: number) => instance.get(`${API_URL_FILES}file`);

const getFolderFiles = (folderId: number) =>
  instance.get(`${API_URL_FILES}file`);

const createFolder = (folderName: string) =>
  instance.post(`${API_URL_FILES}folder/create`, folderName);

const getTrashFiles = () => instance.get(`${API_URL_FILES}trash/file`);

export default { getShelfFiles, getFolderFiles, createFolder, getTrashFiles };
