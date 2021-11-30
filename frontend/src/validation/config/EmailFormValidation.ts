import { emailRegEx } from '../regex';

export const emailFormValidation = {
  required: 'This field is required',
  pattern: {
    value: emailRegEx,
    message: 'Invalid email format',
  },
};
