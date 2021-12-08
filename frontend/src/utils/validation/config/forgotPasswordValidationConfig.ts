import { ForgotPasswordConfig } from '../../interfaces/dataTypes';
import { emailRegex } from '../regex';

export const forgotPasswordFieldConfig: ForgotPasswordConfig = {
  validations: {
    required: 'This field is required',
    pattern: {
      value: emailRegex,
      message: 'Invalid email format',
    },
  },
};
