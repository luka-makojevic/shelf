import { ChangeEventHandler, FocusEventHandler } from 'react';
import { FieldError } from 'react-hook-form';
import { InputFieldType } from '../../../interfaces/dataTypes';

export interface InputStyleProps {
  children?: React.ReactNode;
}

export type InputFieldProps = {
  name?: string;
  placeholder?: string;
  onChange?: ChangeEventHandler;
  error?: FieldError;
  type?: InputFieldType;
  value?: string;
  onFocus?: FocusEventHandler;
  onBlur?: FocusEventHandler;
  disabled?: boolean;
};
