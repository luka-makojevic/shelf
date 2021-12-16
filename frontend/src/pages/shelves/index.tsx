import { useEffect, useState } from 'react';
import { Table } from '../../components/table/table';
import TableWrapper from '../../components/table/TableWrapper';
import { useShelf } from '../../hooks/shelfHooks';
import { TableDataTypes } from '../../interfaces/dataTypes';
import { useAppSelector } from '../../store/hooks';
import AlertPortal from '../../components/alert/alert';
import Modal from '../../components/modal';
import CreateShelfModal from '../../components/modal/createShelfModal';
import { Button } from '../../components/UI/button';
import { AlertMessage } from '../../utils/enums/alertMessages';
import { Description } from '../../components/text/text-styles';

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

  const user = useAppSelector((state) => state.user.user);

  const { getShelves } = useShelf();

  const [shelvesForTable, setShelvesForTable] = useState<TableDataTypes[]>([]);

  useEffect(() => {
    getShelves(
      { userId: user?.id },
      () => {},
      (err: string) => {
        setError(err);
      }
    );
  }, []);

  useEffect(() => {
    const newShelves = shelves.map((shelf) => ({
      name: shelf.name,
      createdAt: new Date(shelf.createdAt).toLocaleDateString('en-US'),
      id: shelf.id,
    }));

    setShelvesForTable(newShelves);
  }, [shelves]);

  const headers = [
    { header: 'Name', key: 'name' },
    { header: 'Creation date', key: 'createdAt' },
  ];

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
        </Modal>
      )}

      <TableWrapper
        title="Shelves"
        description="Shelves are the fundamental containers for data storage."
      >
        <div>
          <Button onClick={handleOpenModal}>Create shelf</Button>
        </div>
        {shelves.length === 0 ? (
          <Description>No shelves</Description>
        ) : (
          <Table
            setTableData={setShelvesForTable}
            data={shelvesForTable}
            headers={headers}
            path="shelves/"
          />
        )}
      </TableWrapper>
    </>
  );
};

export default Shelves;
