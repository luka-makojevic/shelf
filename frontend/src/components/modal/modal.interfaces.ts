import { Dispatch, SetStateAction } from 'react';

export interface CreateShelfModalProps {
  onCloseModal: Dispatch<SetStateAction<boolean>>;
}

export interface AddFileModalProps {
  onCloseModal: Dispatch<SetStateAction<boolean>>;
}

export interface ModalProps {
  onCloseModal: Dispatch<SetStateAction<boolean>>;
  children: JSX.Element;
  title: string;
  closeIcon?: boolean;
}
