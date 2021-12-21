import { API_URL_FILESYSTEM } from '../api/api';
import instance from '../api/axiosInstance';

const getShelves = () => instance.get(`${API_URL_FILESYSTEM}shelf`);

const createShelf = (shelfName: string) =>
  instance.post(`${API_URL_FILESYSTEM}shelf/create`, shelfName);

const deleteShelf = (shelfIds: number[]) =>
  instance.put(`${API_URL_FILESYSTEM}shelf/move-to-trash`, shelfIds);

const editShelf = (id: number, data: string) =>
  instance.post(`${API_URL_FILESYSTEM}${id}`, data);

export default { getShelves, createShelf, deleteShelf, editShelf };
