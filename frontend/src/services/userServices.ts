import axios from 'axios';

const API_URL = `http://10.10.0.117:8080/tokens`;

const emailConfirmation = (token: string | undefined) =>
  axios.post(`${API_URL}/confirmation`, { token });

const resendEmailVerification = (token: string | undefined) =>
  axios.post(`${API_URL}/resend`, { token });

export default {
  emailConfirmation,
  resendEmailVerification,
};
