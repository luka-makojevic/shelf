import instance from '../api/axiosInstance';
import { FunctionFormData } from '../interfaces/dataTypes';

const API_URL_FUNCTIONS = '/shelffunctions';

const createPredefinedFunction = (data: FunctionFormData) =>
  instance.post(`${API_URL_FUNCTIONS}/functions/predefined`, data);

const getFunctions = () => instance.get(`${API_URL_FUNCTIONS}/functions`);

const deleteFunction = (functionId: number) =>
  instance.delete(`${API_URL_FUNCTIONS}/functions/${functionId}`);

const createCustomfunction = (data: FunctionFormData) =>
  instance.post(`${API_URL_FUNCTIONS}/functions/custom`, data);

const renameFunction = (data: { functionId: number; newName: string }) =>
  instance.put(`${API_URL_FUNCTIONS}/functions/rename`, data);
const getFunction = (functionId: number) =>
  instance.get(`${API_URL_FUNCTIONS}/functions/${functionId}`);

const saveFunction = (functionId: number, code: string) =>
  instance.put(`shelffunctions/functions/code`, {
    functionId,
    code,
  });

const executeFunction = (language: string, functionId: number) =>
  instance.get(
    `shelffunctions/execute/language/${language}/function/${functionId}`
  );

export default {
  createPredefinedFunction,
  getFunctions,
  deleteFunction,
  createCustomfunction,
  renameFunction,
  getFunction,
  saveFunction,
  executeFunction,
};
