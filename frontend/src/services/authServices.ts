import axios from "axios"

const API_URL = ""


export const httpLogin = (data:any) => {
    return axios
      .post(`${API_URL}register`, {
        data,
      })
      .then((res) => res)
}
export const httpRegister = (data:any) => {
    return axios
      .post(`${API_URL}register`, {
        data,
      })
      .then((res) => res)
}