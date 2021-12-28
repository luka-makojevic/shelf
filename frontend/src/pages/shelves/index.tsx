import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { Table } from '../../components/table/table';
import TableWrapper from '../../components/table/TableWrapper';
import { ShelfDataType, TableDataTypes } from '../../interfaces/dataTypes';
import ShelfModal from '../../components/modal/shelfModal';
import Modal from '../../components/modal';
import { Button } from '../../components/UI/button';
import { Description } from '../../components/text/text-styles';
import DeleteShelfModal from '../../components/modal/deleteShelfModal';
import SearchBar from '../../components/UI/searchBar/searchBar';
import shelfServices from '../../services/shelfServices';

const headers = [
  { header: 'Name', key: 'name' },
  { header: 'Creation date', key: 'createdAt' },
];

const Shelves = () => {
  const [shelves, setShelves] = useState<ShelfDataType[]>([]);

  const [openModal, setOpenModal] = useState(false);
  const [selectedShelf, setSelectedShelf] = useState<TableDataTypes | null>(
    null
  );

  const [shelvesForTable, setShelvesForTable] = useState<TableDataTypes[]>([]);
  const [filteredShelves, setFilteredShelves] = useState<TableDataTypes[]>([]);

  const [deleteModalOpen, setDeleteModalOpen] = useState<boolean>(false);

  const getData = () => {
    shelfServices
      .getShelves()
      .then((res) => setShelves(res.data))
      .catch((err) => toast.error(err));
  };

  useEffect(() => {
    getData();
  }, []);

  useEffect(() => {
    const newShelves = shelves.map((shelf) => ({
      name: shelf.name,
      createdAt: new Date(shelf.createdAt).toLocaleString('en-US'),
      id: shelf.id,
    }));

    setShelvesForTable(newShelves);
    setFilteredShelves(newShelves);
  }, [shelves]);

  const handleModalClose = () => {
    setDeleteModalOpen(false);
    setSelectedShelf(null);
    setOpenModal(false);
  };

  const handleOpenDeleteModal = (shelf: TableDataTypes) => {
    setDeleteModalOpen(true);
    setSelectedShelf(shelf);
  };

  const handleDelete = (shelf: TableDataTypes) => {
    const newShelves = shelves.filter((item) => item.id !== shelf.id);
    setShelves(newShelves);
  };

  const handleEdit = (shelf: TableDataTypes, newName: string) => {
    const newShelves = shelves.map((item) => {
      if (item.id === shelf.id) {
        return { ...item, name: newName };
      }
      return item;
    });
    setShelves(newShelves);
  };

  const handleOpenEditModal = (shelf: TableDataTypes) => {
    setSelectedShelf(shelf);
    setOpenModal(true);
  };

  const handleOpenModal = () => {
    setOpenModal(true);
  };

  const message =
    shelves.length === 0
      ? 'No shelves have been created yet'
      : 'Sorry, no matching results found';

  return (
    <>
      {openModal && (
        <Modal
          title={selectedShelf ? 'Rename shelf' : 'Create shelf'}
          onCloseModal={handleModalClose}
          closeIcon
        >
          <ShelfModal
            onGetData={getData}
            onCloseModal={handleModalClose}
            shelf={selectedShelf}
            onEdit={handleEdit}
          />
        </Modal>
      )}

      {deleteModalOpen && (
        <Modal title="Delete shelf" onCloseModal={handleModalClose}>
          <DeleteShelfModal
            onDelete={handleDelete}
            onCloseModal={handleModalClose}
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
            onDelete={handleOpenDeleteModal}
            onEdit={handleOpenEditModal}
          />
        )}
      </TableWrapper>
    </>
  );
};

export default Shelves;
