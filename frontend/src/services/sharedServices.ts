import instance from '../api/axiosInstance';

const API_URL_SHARED = 'http://10.10.0.136:8080/filesystem/';

const getSharedFiles = () => instance.get(`${API_URL_SHARED}shared`);

export default { getSharedFiles };
