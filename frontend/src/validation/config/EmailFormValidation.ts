import { emailRegex } from '../regex';

export const emailFormValidation = {
  required: 'This field is required',
  pattern: {
    value: emailRegex,
    message: 'Invalid email format',
  },
};
