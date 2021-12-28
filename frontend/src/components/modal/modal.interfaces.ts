import { Dispatch, SetStateAction } from 'react';
import { TableDataTypes } from '../../interfaces/dataTypes';

export interface ShelfModalProps {
  onGetData: () => void;
  onEdit: (data: TableDataTypes, newName: string) => void;
  onCloseModal: Dispatch<SetStateAction<boolean>>;
  shelf: TableDataTypes | null;
}

export interface DeleteShelfModalProps {
  onDelete: (shelf: TableDataTypes) => void;
  onCloseModal: Dispatch<SetStateAction<boolean>>;
  shelf: TableDataTypes | null;
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
  shelfId: string | undefined;
  folderId: string | undefined;
  placeholder: string | undefined;
  buttonText: string | undefined;
  file?: TableDataTypes | null;
  onGetData: () => void;
  onEdit: (file: TableDataTypes, newName: string) => void;
}
