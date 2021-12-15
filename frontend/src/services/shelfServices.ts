import instance from '../api/axiosInstance';

const API_URL_SHELF = 'http://10.10.0.136:8080/filesystem/';

const getShelves = () => instance.get(`${API_URL_SHELF}shelf`);

const createShelf = (shelfName: string) =>
  instance.post(`${API_URL_SHELF}shelf/create`, shelfName);

const deleteShelf = (data: { shelfId: string }) =>
  instance.put(`${API_URL_SHELF}shelf`, data);

const editShelf = (id: string, data: string) =>
  instance.post(`${API_URL_SHELF}${id}`, data);

export default { getShelves, createShelf, deleteShelf, editShelf };
