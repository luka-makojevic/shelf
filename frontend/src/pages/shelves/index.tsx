import { useEffect, useState } from 'react';
import { Table } from '../../components/table/table';
import TableWrapper from '../../components/table/TableWrapper';
import { useShelf } from '../../hooks/shelfHooks';
import { TableDataTypes } from '../../interfaces/dataTypes';
import { useAppSelector } from '../../store/hooks';
import AlertPortal from '../../components/alert/alert';
import CreateShelfModal from '../../components/modal/createShelfModal';
import Modal from '../../components/modal';
import { Button } from '../../components/UI/button';
import { AlertMessage } from '../../utils/enums/alertMessages';
import { Description } from '../../components/text/text-styles';
import DeleteShelfModal from '../../components/modal/deleteShelfModal';
import SearchBar from '../../components/UI/searchBar/searchBar';

const Shelves = () => {
  const shelves = useAppSelector((state) => state.shelf.shelves);

  const [openModal, setOpenModal] = useState(false);
  const [error, setError] = useState('');
  const [selectedShelf, setSelectedShelf] = useState<TableDataTypes | null>(
    null
  );

  const handleOpenModal = () => {
    setOpenModal(true);
  };

  const handleSetError = () => {
    setError('');
  };

  const user = useAppSelector((state) => state.user.user);

  const { getShelves } = useShelf();

  const [shelvesForTable, setShelvesForTable] = useState<TableDataTypes[]>([]);
  const [filteredShelves, setFilteredShelves] = useState<TableDataTypes[]>([]);

  const [deleteModalOpen, setDeleteModalOpen] = useState<boolean>(false);

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
    setFilteredShelves(newShelves);
  }, [shelves]);

  const headers = [
    { header: 'Name', key: 'name' },
    { header: 'Creation date', key: 'createdAt' },
  ];

  const handleModalClose = () => {
    setDeleteModalOpen(false);
  };

  const handleDelete = (shelf: TableDataTypes) => {
    setDeleteModalOpen(true);
    setSelectedShelf(shelf);
  };

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
      {deleteModalOpen && (
        <Modal title="Delete shelf" onCloseModal={handleModalClose}>
          <DeleteShelfModal
            onCloseModal={handleModalClose}
            onError={setError}
            shelf={selectedShelf}
          />
        </Modal>
      )}

      <TableWrapper
        title="Shelves"
        description="Shelves are the fundamental containers for data storage."
      >
        <div>
          <SearchBar
            placeholder="Search..."
            data={shelvesForTable}
            setData={setFilteredShelves}
            searchKey="name"
          />
          <Button onClick={handleOpenModal}>Create shelf</Button>
        </div>
        {shelves.length === 0 ? (
          <Description>No shelves</Description>
        ) : (
          <Table
            setTableData={setFilteredShelves}
            data={filteredShelves}
            headers={headers}
            path="shelves/"
            onDelete={handleDelete}
          />
        )}
      </TableWrapper>
    </>
  );
};

export default Shelves;
