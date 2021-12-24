import { useEffect, useState } from 'react';
import { Table } from '../../components/table/table';
import { useShelf } from '../../hooks/shelfHooks';
import { TableDataTypes } from '../../interfaces/dataTypes';
import { useAppSelector } from '../../store/hooks';
import AlertPortal from '../../components/alert/alert';
import ShelfModal from '../../components/modal/shelfModal';
import Modal from '../../components/modal';
import { Button } from '../../components/UI/button';
import { AlertMessage } from '../../utils/enums/alertMessages';
import { Description } from '../../components/text/text-styles';
import DeleteShelfModal from '../../components/modal/deleteMessageModal';
import SearchBar from '../../components/UI/searchBar/searchBar';
import TableWrapper from '../../components/table/tableWrapper';

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

  const { getShelves } = useShelf();

  const [shelvesForTable, setShelvesForTable] = useState<TableDataTypes[]>([]);
  const [filteredShelves, setFilteredShelves] = useState<TableDataTypes[]>([]);

  const [deleteModalOpen, setDeleteModalOpen] = useState<boolean>(false);

  useEffect(() => {
    getShelves(
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
    setSelectedShelf(null);
    setOpenModal(false);
  };

  const handleDelete = (shelf: TableDataTypes) => {
    setDeleteModalOpen(true);
    setSelectedShelf(shelf);
  };

  const handleEdit = (shelf: TableDataTypes) => {
    setSelectedShelf(shelf);
    setOpenModal(true);
  };

  const message =
    shelves.length === 0
      ? 'No shelves have been created yet'
      : 'Sorry, no matching results found';

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
        <Modal
          title={selectedShelf ? 'Rename shelf' : 'Create shelf'}
          onCloseModal={handleModalClose}
          closeIcon
        >
          <ShelfModal
            onCloseModal={handleModalClose}
            onError={setError}
            shelf={selectedShelf}
          />
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
        {shelves.length === 0 || filteredShelves.length === 0 ? (
          <Description>{message}</Description>
        ) : (
          <Table
            setTableData={setFilteredShelves}
            data={filteredShelves}
            headers={headers}
            path="shelves/"
            onDelete={handleDelete}
            onEdit={handleEdit}
          />
        )}
      </TableWrapper>
    </>
  );
};

export default Shelves;
