import { Dispatch, SetStateAction } from 'react';

export interface UploadModalProps {
  onCloseModal: Dispatch<SetStateAction<boolean>>;
}

export interface DropZoneWrapperProps {
  isDragOver: boolean;
}
