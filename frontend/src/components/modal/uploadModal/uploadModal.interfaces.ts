import { Dispatch, SetStateAction } from 'react';

export interface UploadModalProps {
  onCloseModal: Dispatch<SetStateAction<boolean>>;
  onError: (value: SetStateAction<string>) => void;
  onSuccess: (value: SetStateAction<string>) => void;
}

export interface DropZoneWrapperProps {
  isDragOver: boolean;
}
