import { ForgotPasswordConfig } from '../../interfaces/types';
import { emailRegEx } from '../regex';

export const forgotPasswordFieldConfig: ForgotPasswordConfig = {
  validations: {
    required: 'This field is required',
    pattern: {
      value: emailRegEx,
      message: 'Invalid email format',
    },
  },
};
