import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { FaPlusCircle } from 'react-icons/fa';
import { Table } from '../../components/table/table';
import {
  ShelfDataType,
  ShelfFormData,
  TableDataTypes,
} from '../../interfaces/dataTypes';
import { Button } from '../../components/UI/button';
import { Description } from '../../components/text/text-styles';
import SearchBar from '../../components/UI/searchBar/searchBar';
import shelfServices from '../../services/shelfServices';
import {
  ActionsBox,
  ButtonActionsBox,
} from '../../components/table/tableWrapper.styles';
import TableWrapper from '../../components/table/TableWrapper';
import {
  ActionType,
  Delete,
  Edit,
} from '../../components/table/table.interfaces';
import { ModifyModal } from '../../components/modal/modifyModal';
import DeleteModal from '../../components/modal/deleteModal';

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
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [shelvesForTable, setShelvesForTable] = useState<TableDataTypes[]>([]);
  const [filteredShelves, setFilteredShelves] = useState<TableDataTypes[]>([]);

  const [deleteModalOpen, setDeleteModalOpen] = useState<boolean>(false);

  const getData = () => {
    setIsLoading(true);
    shelfServices
      .getShelves()
      .then((res) => setShelves(res.data))
      .catch((err) => toast.error(err))
      .finally(() => {
        setIsLoading(false);
      });
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

  const handleDelete = () => {
    shelfServices
      .hardDeleteShelf(selectedShelf?.id)
      .then((res) => {
        const newShelves = shelves.filter(
          (item) => item.id !== selectedShelf?.id
        );
        setShelves(newShelves);
        handleModalClose();
        toast.success(res.data.message);
      })
      .catch((err) => {
        toast.error(err.response?.data?.message);
      });
  };

  const handleEdit = (newName: string) => {
    const newShelves = shelves.map((item) => {
      if (item.id === selectedShelf?.id) {
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

  const actions: ActionType[] = [
    { comp: Delete, handler: handleOpenDeleteModal, key: 1 },
    { comp: Edit, handler: handleOpenEditModal, key: 2 },
  ];

  const message =
    shelves.length === 0
      ? 'No shelves have been created yet'
      : 'Sorry, no matching results found';

  const onSubmit = (data: ShelfFormData) => {
    const shelfName = data.name.trim();
    if (!selectedShelf) {
      shelfServices
        .createShelf(shelfName)
        .then((res) => {
          getData();
          toast.success(res.data.message);
        })
        .catch((err) => {
          toast.error(err.response?.data?.message);
        });
    } else {
      shelfServices
        .editShelf({ shelfId: selectedShelf.id, shelfName: data.name })
        .then((res) => {
          handleEdit(shelfName);
          toast.success(res.data.message);
        })
        .catch((err) => {
          toast.error(err.response?.data?.message);
        });
    }
    handleModalClose();
  };

  if (isLoading) return null;

  return (
    <>
      {openModal && (
        <ModifyModal
          title={selectedShelf ? 'Rename shelf' : 'Create shelf'}
          onCloseModal={handleModalClose}
          onSubmit={onSubmit}
          buttonMessage={selectedShelf ? 'Rename ' : 'Create '}
          placeHolder={selectedShelf ? 'Rename Shelf' : 'Untilted Shelf'}
          defaultValue={selectedShelf?.name}
        />
      )}
      {deleteModalOpen && (
        <DeleteModal
          title="Delete shelf"
          onCloseModal={handleModalClose}
          onDelete={handleDelete}
          message={`Are you sure you want to delete '${selectedShelf?.name}' shelf? This action will permanently delete all data inside this shelf!`}
        />
      )}

      <TableWrapper
        title="Shelves"
        description="Shelves are the fundamental containers for data storage."
      >
        <ActionsBox>
          <SearchBar
            placeholder="Search..."
            data={shelvesForTable}
            setData={setFilteredShelves}
            searchKey="name"
          />
          <ButtonActionsBox>
            <Button icon={<FaPlusCircle />} onClick={handleOpenModal}>
              Create shelf
            </Button>
          </ButtonActionsBox>
        </ActionsBox>
        {shelves.length === 0 || filteredShelves.length === 0 ? (
          <Description>{message}</Description>
        ) : (
          <Table
            setTableData={setFilteredShelves}
            data={filteredShelves}
            headers={headers}
            path="shelves/"
            actions={actions}
          />
        )}
      </TableWrapper>
    </>
  );
};

export default Shelves;
