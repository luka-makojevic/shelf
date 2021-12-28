/* eslint-disable @typescript-eslint/no-explicit-any */
import { Method } from 'axios';
import { useState } from 'react';
import instance from './axiosInstance';

const makeParams = (method: Method, url: string, data?: any) => ({
  method,
  url,
  data,
});

export default function useApi(url: string) {
  const [data, setResponseData] = useState<any>();
  const [isQuerying, setIsQuerying] = useState(false);

  const get = async () => {
    setIsQuerying(true);
    const res = await instance.request(makeParams('GET', url));
    const resData = await res.data;
    setResponseData(resData);
    setIsQuerying(false);
  };

  const add = async (nekiData: any) => {
    setIsQuerying(true);
    const res = await instance.request(makeParams('POST', url, nekiData));
    const resData = await res.data;

    setResponseData(resData);
    setIsQuerying(false);
  };

  //   const edit = async ({
  //     id,
  //     newTitle,
  //     data,
  //     method = 'POST',
  //   }: {
  //     id: number;
  //     newTitle: string;
  //     extension: string;
  //     data: any;
  //     method: Method;
  //   }) => {
  //     setIsQuerying(true);
  //     await instance.request(makeParams(data, method));
  //     const copy = [...data];
  //     const index = copy.findIndex((item) => item.id === id);
  //     copy[index].title = newTitle;
  //     setResponseData(copy);
  //     setIsQuerying(false);
  //   };
  //   const remove = async (
  //     id: number,
  //     { extension, data, method = 'POST' }: any
  //   ) => {
  //     setIsQuerying(true);
  //     await instance.request(makeParams(extension, data, method));
  //     const copy = [...data];
  //     copy.splice(
  //       copy.findIndex((item) => item.id === id),
  //       1
  //     );
  //     setResponseData(copy);
  //     setIsQuerying(false);
  //   };

  const api = {
    get,
    add,
    // edit,
    // remove,
  };
  return { data, isQuerying, api };
}
