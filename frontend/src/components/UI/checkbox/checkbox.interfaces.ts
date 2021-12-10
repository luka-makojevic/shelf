import { ChangeEventHandler } from 'react';

export type CheckBoxProps = {
  onChange?: ChangeEventHandler;
  name?: string;
  id?: string;
  children?: React.ReactNode;
  checked?: boolean;
};
