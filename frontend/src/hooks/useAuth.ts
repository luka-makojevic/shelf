import axios from "axios"

const API_URL = "http://10.10.0.120/8080/"

export const useAuthService = () =>  {

    const register = (email:string,password:string, firstName:string, lastName:string) => {
  return axios
    .post(`${API_URL}users`, {
        email,
        password,
        firstName,
        lastName,

    })
}

 
    return { register }
}

