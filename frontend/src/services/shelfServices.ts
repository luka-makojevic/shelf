import instance from '../api/axiosInstance';

const API_URL = 'http://10.10.0.136:8080/file-system/';

const getShelves = (data: { userId?: number }) =>
  instance.get(`${API_URL}shelf/${data.userId}`);

const createShelf = (data: { name: string; userId: number }) =>
  instance.post(`${API_URL}shelf`, data);

const deleteShelf = (data: { shelfId: string }) =>
  instance.put(`${API_URL}shelf`, data);

export default { getShelves, createShelf, deleteShelf };
