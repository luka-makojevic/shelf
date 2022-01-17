import instance from '../api/axiosInstance';

const API_URL_ACCOUNT = '/account';

const getUsers = () => instance.get(`${API_URL_ACCOUNT}/admin?size=20`);

const deleteUser = (userId: number) =>
  instance.delete(`${API_URL_ACCOUNT}/admin/${userId}`);

const changeUserRole = (userId: number, roleId: number) =>
  instance.put(`${API_URL_ACCOUNT}/admin/grant/${userId}`, { roleId });

export default {
  getUsers,
  deleteUser,
  changeUserRole,
};
