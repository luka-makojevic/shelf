import instance from '../api/axiosInstance';

const API_URL_SHELF = 'http://10.10.0.136:8080/filesystem/';

const getShelves = () => instance.get(`${API_URL_SHELF}shelf`);

const createShelf = (shelfName: string) =>
  instance.post(`${API_URL_SHELF}shelf`, shelfName);

const softDeleteShelf = (shelfIds: number[]) =>
  instance.put(`${API_URL_SHELF}shelf/move-to-trash`, shelfIds);

const hardDeleteShelf = (shelfId: number) =>
  instance.delete(`${API_URL_SHELF}shelf/${shelfId}`);

const editShelf = (data: { shelfId: number; shelfName: string }) =>
  instance.put(`${API_URL_SHELF}shelf/rename`, data);

export default {
  getShelves,
  createShelf,
  softDeleteShelf,
  editShelf,
  hardDeleteShelf,
};
