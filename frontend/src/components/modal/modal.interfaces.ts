import { Dispatch, SetStateAction } from 'react';
import { TableDataTypes } from '../../interfaces/dataTypes';

export interface ShelfModalProps {
  onCloseModal: Dispatch<SetStateAction<boolean>>;
  onError: (value: SetStateAction<string>) => void;
  shelf: TableDataTypes | null;
}

export interface DeleteShelfModalProps {
  onCloseModal: () => void;
  onError: (value: SetStateAction<string>) => void;
  onDelete: (file: TableDataTypes[]) => void;
  shelf?: TableDataTypes | null;
  message?: string;
  selectedData?: TableDataTypes[];
}

export interface PathType {
  path: { name: string; id: number | null }[];
}

export interface ModalProps {
  onCloseModal: Dispatch<SetStateAction<boolean>>;
  children: JSX.Element;
  title: string;
  closeIcon?: boolean;
}

export interface FolderModalProps {
  onCloseModal: Dispatch<SetStateAction<boolean>>;
  onError: (value: SetStateAction<string>) => void;
  shelfId: string | undefined;
  folderId: string | undefined;
  placeholder: string | undefined;
  buttonText: string | undefined;
  file?: TableDataTypes | null;
  getData: () => void;
}
