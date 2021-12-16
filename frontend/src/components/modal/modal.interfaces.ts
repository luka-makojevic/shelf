import { Dispatch, SetStateAction } from 'react';
import { TableDataTypes } from '../../interfaces/dataTypes';

export interface CreateShelfModalProps {
  onCloseModal: Dispatch<SetStateAction<boolean>>;
  onError: (value: SetStateAction<string>) => void;
}

export interface DeleteShelfModalProps {
  onCloseModal: Dispatch<SetStateAction<boolean>>;
  onError: (value: SetStateAction<string>) => void;
  shelf: TableDataTypes | null;
}

export interface PathType {
  path: { name: string; id: number | null }[];
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
