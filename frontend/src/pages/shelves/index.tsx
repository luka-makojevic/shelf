import { useState } from 'react';
import TableWrapper from '../../components/table/TableWrapper';
// import { useShelf } from '../../hooks/shelfHooks';
import { useAppSelector } from '../../store/hooks';
import AlertPortal from '../../components/alert/alert';
import Modal from '../../components/modal';
import CreateShelfModal from '../../components/modal/createShelfModal';
import { Button } from '../../components/UI/button';
import { AlertMessage } from '../../utils/enums/alertMessages';

const Shelves = () => {
  const shelves = useAppSelector((state) => state.shelf.shelves);

  const [openModal, setOpenModal] = useState(false);
  const [error, setError] = useState('');

  const handleOpenModal = () => {
    setOpenModal(true);
  };

  const handleSetError = () => {
    setError('');
  };

  // const user = useAppSelector((state) => state.user.user);

  // const { getShelves } = useShelf();

  // const [shelvesForTable, setShelvesForTable] = useState<any>();

  // useEffect(() => {
  //   const newShelves = shelves.map((shelf) => ({
  //     name: shelf.name,
  //     id: shelf.id,
  //   }));

  //   setShelvesForTable(newShelves);
  // }, [shelves]);

  // useEffect(() => {
  //   getShelves(
  //     { userId: user?.id },
  //     () => {
  //       console.log('success');
  //     },
  //     (err: string) => {
  //       console.log(err, 'error');
  //     }
  //   );
  // }, []);

  return (
    <>
      {error && (
        <AlertPortal
          type={AlertMessage.ERRROR}
          title="Error"
          message={error}
          onClose={handleSetError}
        />
      )}
      {openModal && (
        <Modal title="Create shelf" onCloseModal={setOpenModal} closeIcon>
          <CreateShelfModal onCloseModal={setOpenModal} onError={setError} />
          {/* <AddFileModal onCloseModal={setOpenModal} /> */}
        </Modal>
      )}

      <TableWrapper
        title="Shelves"
        description="Shelves are the fundamental containers for data storage."
      >
        {shelves.length === 0 ? (
          <p>no shelfs</p>
        ) : (
          <div>
            <Button onClick={handleOpenModal}>create shelf</Button>
            <div>Table</div>
          </div>
        )}
      </TableWrapper>
    </>
  );
};

export default Shelves;
