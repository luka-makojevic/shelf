import instance from '../api/axiosInstance';

const API_URL_FILESYSTEM = '/filesystem';

const getFolderFiles = (folderId: number) =>
  instance.get(`${API_URL_FILESYSTEM}/folder/${folderId}`);

const createFolder = (
  folderName: string,
  shelfId: number,
  parentFolderId = 0
) =>
  instance.post(`${API_URL_FILESYSTEM}/folder`, {
    folderName,
    shelfId,
    parentFolderId,
  });

const softDeleteFolder = (folderIds: number[]) =>
  instance.put(`${API_URL_FILESYSTEM}/folder/move-to-trash`, folderIds);

const hardDeleteFolder = (data: number[]) =>
  instance.delete(`${API_URL_FILESYSTEM}/folder`, { data });
  
const recoverFolderFromTrash = (data: number[]) =>
  instance.put(`${API_URL_FILESYSTEM}/folder/recover`, data);

const editFolder = (data: { folderId: number; folderName: string }) =>
  instance.put(`${API_URL_FILESYSTEM}/folder/rename`, data);

export default {
  getFolderFiles,
  createFolder,
  softDeleteFolder,
  hardDeleteFolder,
  recoverFolderFromTrash,
  editFolder,
};
