import { FaFolderOpen } from 'react-icons/fa';
import { AddFileModalProps } from '../../interfaces/types';
import { theme } from '../../theme';
import { Button } from '../UI/button';
import { Footer } from './modal-styles';

const AddFileModal = ({ onCloseModal }: AddFileModalProps) => {
  const handleAddFile = () => {};

  const handleUpload = () => {
    onCloseModal(false);
  };

  return (
    <>
      <FaFolderOpen size={theme.space[6]} />
      <Button variant="lightBordered" onClick={handleAddFile}>
        Add files
      </Button>
      <Footer>
        <Button onClick={handleUpload} disabled>
          Upload
        </Button>
      </Footer>
    </>
  );
};

export default AddFileModal;
