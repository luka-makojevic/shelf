import instance from '../api/axiosInstance';

const API_URL_FILESYSTEM = '/filesystem';

const getTrash = () => instance.get(`${API_URL_FILESYSTEM}/shelf/trash`);

const getTrashFiles = (folderId: number) =>
  instance.get(`${API_URL_FILESYSTEM}/folder/trash/${folderId}`);

export default {
  getTrash,
  getTrashFiles,
};
