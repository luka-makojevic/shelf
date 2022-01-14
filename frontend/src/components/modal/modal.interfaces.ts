import { Dispatch, SetStateAction } from 'react';
import { TableDataTypes } from '../../interfaces/dataTypes';

export interface ShelfModalProps {
  onGetData: () => void;
  onEdit: (data: TableDataTypes, newName: string) => void;
  onCloseModal: Dispatch<SetStateAction<boolean>>;
  shelf: TableDataTypes | null;
}

export interface DeleteModalProps {
  onCloseModal: () => void;
  onDeleteShelf?: (shelf: TableDataTypes) => void;
  onDeleteFiles?: (files: TableDataTypes[]) => void;
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
export interface ModalStyleProps {
  background?: string;
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

export interface ShelvesOptionTypes {
  text: string;
  value: string;
}

export interface FunctionModalProps {
  onGetData: () => void;
  onCloseModal: Dispatch<SetStateAction<boolean>>;
  data: ShelvesOptionTypes[];
}

export interface FunctionStyleProps {
  for: string;
}
