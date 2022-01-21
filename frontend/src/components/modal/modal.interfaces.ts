import { Dispatch, SetStateAction } from 'react';
import { TableDataTypes } from '../../interfaces/dataTypes';

export interface ShelfModalProps {
  onGetData: () => void;
  onEdit: (data: TableDataTypes, newName: string) => void;
  onCloseModal: Dispatch<SetStateAction<boolean>>;
  shelf: TableDataTypes | null;
}
export interface ModifyModalProps {
  onCloseModal: () => void;
  onSubmit: (data: { name: string }) => void;
  selectedData?: TableDataTypes;
  title: string;
  buttonMessage: string;
  placeHolder: string;
  defaultValue: string | undefined;
}

export interface DeleteModalProps {
  onCloseModal: () => void;
  onDelete: () => void;
  message?: string;
  title: string;
}

export interface PathType {
  path: { name: string; id: number | null }[];
}

export interface ModalProps {
  onCloseModal: () => void;
  children: JSX.Element;
  title?: string;
  closeIcon?: boolean;
  background?: string;
  color?: string;
}
export interface ModalStyleProps {
  background?: string;
  color?: string;
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
  onCloseModal: () => void;
  data: ShelvesOptionTypes[];
}

export interface FunctionStyleProps {
  htmlFor: string;
}

export interface FunctionEditModalProps {
  onCloseModal: Dispatch<SetStateAction<boolean>>;
  functionData?: TableDataTypes | null;
  onEdit: (functionData: TableDataTypes, newName: string) => void;
  onGetData: () => void;
}
