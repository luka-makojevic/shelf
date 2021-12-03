import { useState } from 'react';
import { Header } from '../../components';
import Modal from '../../components/modal';
import AddFileModal from '../../components/modal/addFileModal';
import CreateShelfModal from '../../components/modal/createShelfModal';
import { Button } from '../../components/UI/button';

const Dashboard = () => {
  const hideProfile = true;
  const [isOpenAddFile, setIsOpenAddFile] = useState<boolean>(false);
  const [isOpenCreateShelf, setIsOpenCreateShelf] = useState<boolean>(false);

  const handleOpenAddModal = () => {
    setIsOpenAddFile(true);
  };

  const handleOpenCreateShelfModal = () => {
    setIsOpenCreateShelf(true);
  };

  return (
    <>
      <Header hideProfile={hideProfile} />
      <div
        style={{ height: '200px', display: 'flex', justifyContent: 'center' }}
      >
        <Button variant="lightBordered" onClick={handleOpenAddModal}>
          Add File
        </Button>
        <Button onClick={handleOpenCreateShelfModal}>Create Shelf</Button>
      </div>
      {isOpenAddFile && (
        <Modal onCloseModal={setIsOpenAddFile} title="Upolad File" closeIcon>
          <AddFileModal onCloseModal={setIsOpenAddFile} />
        </Modal>
      )}
      {isOpenCreateShelf && (
        <Modal onCloseModal={setIsOpenCreateShelf} title="Upolad File">
          <CreateShelfModal onCloseModal={setIsOpenCreateShelf} />
        </Modal>
      )}
    </>
  );
};

export default Dashboard;
