import { API_URL_FILESYSTEM } from '../api/api';
import instance from '../api/axiosInstance';

const getShelves = () => instance.get(`${API_URL_FILESYSTEM}shelf`);

const createShelf = (shelfName: string) =>
  instance.post(`${API_URL_FILESYSTEM}shelf`, shelfName);

const softDeleteShelf = (shelfIds: number[]) =>
  instance.put(`${API_URL_FILESYSTEM}shelf/move-to-trash`, shelfIds);

const hardDeleteShelf = (shelfId: number) =>
  instance.delete(`${API_URL_FILESYSTEM}shelf/${shelfId}`);

const editShelf = (data: { shelfId: number; shelfName: string }) =>
  instance.put(`${API_URL_FILESYSTEM}shelf/rename`, data);

const getTrash = () => instance.get(`${API_URL_FILESYSTEM}shelf/trash`);

export default {
  getShelves,
  createShelf,
  softDeleteShelf,
  editShelf,
  hardDeleteShelf,
  getTrash,
};
