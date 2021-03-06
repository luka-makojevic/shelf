import { FieldValues, UseFormWatch } from 'react-hook-form';
import { whiteSpaceRegex } from '../regex';

export const config = (watch: UseFormWatch<FieldValues>) => {
  const functionValidation = {
    required: 'This field is required',
    maxLength: {
      value: 50,
      message: 'Function name can not be longer than 50 characters',
    },
    pattern: {
      value: whiteSpaceRegex,
      message: 'Invalid name format',
    },
  };
  const basicValidation = {
    required: 'This field is required',
  };

  const backupShelf = {
    required: 'This field is required',
    validate: (value: string) =>
      value !== watch('shelfId') || "Can't select the same shelf",
  };

  return { functionValidation, basicValidation, backupShelf };
};
