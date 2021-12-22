import { FaFolderOpen } from 'react-icons/fa';
import { theme } from '../../theme';
import { Button } from '../UI/button';
import { Footer } from './modal.styles';
import { AddFileModalProps } from './modal.interfaces';

const AddFileModal = ({ onCloseModal }: AddFileModalProps) => {
  const handleAddFile = () => {};

  const handleUpload = () => {
    onCloseModal(false);
  };

  return (
    <>
      <FaFolderOpen size={theme.size.lg} />
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
