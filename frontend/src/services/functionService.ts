import instance from '../api/axiosInstance';
import {  FunctionFormData } from '../interfaces/dataTypes';

const API_URL_FUNCTIONS = '/shelffunctions';

const createPredefinedFunction = (data: FunctionFormData) =>
  instance.post(`${API_URL_FUNCTIONS}/functions/predefined`, data);

const getFunctions = () => instance.get(`${API_URL_FUNCTIONS}/functions`);

export default {
  createPredefinedFunction,
  getFunctions,
};
