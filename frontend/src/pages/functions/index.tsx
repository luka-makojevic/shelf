import { useEffect, useState } from 'react';
import { FaPlusCircle } from 'react-icons/fa';
import { toast } from 'react-toastify';
import Modal from '../../components/modal';
import DeleteModal from '../../components/modal/deleteModal';
import FunctionEditModal from '../../components/modal/editFunctionModal';
import FunctionModal from '../../components/modal/functionModal';
import { ShelvesOptionTypes } from '../../components/modal/modal.interfaces';
import { Table } from '../../components/table/table';
import {
  ActionType,
  Delete,
  Edit,
} from '../../components/table/table.interfaces';
import TableWrapper from '../../components/table/TableWrapper';
import {
  ActionsBox,
  ButtonActionsBox,
} from '../../components/table/tableWrapper.styles';
import { Description } from '../../components/text/text-styles';
import { Button } from '../../components/UI/button';
import SearchBar from '../../components/UI/searchBar/searchBar';
import { TableDataTypes } from '../../interfaces/dataTypes';
import functionService from '../../services/functionService';
import shelfServices from '../../services/shelfServices';

const Functions = () => {
  const [filteredFunction, setFilteredFunctions] = useState<TableDataTypes[]>(
    []
  );
  const [openModal, setOpenModal] = useState(false);
  const [openDeleteModal, setOpenDeleteModal] = useState<boolean>();
  const [selectedFunction, setSelectedFunction] =
    useState<TableDataTypes | null>(null);
  const [shelvesOptions, setShelvesOptions] = useState<ShelvesOptionTypes[]>(
    []
  );
  const [openEditModal, setOpenEditModal] = useState(false);
  const [functionsForTable, setFunctionsForTable] = useState<TableDataTypes[]>(
    []
  );
  const [isLoading, setIsLoading] = useState(false);

  const getData = () => {
    setIsLoading(true);
    shelfServices
      .getShelves()
      .then((response) => {
        setShelvesOptions(
          response.data?.map((shelf: TableDataTypes) => ({
            text: shelf.name,
            value: shelf.id,
          }))
        );
        functionService
          .getFunctions()
          .then((res) => {
            const newData = res.data.map((item: TableDataTypes) => ({
              name: item.name,
              shelfName: response.data?.find(
                (shelf: TableDataTypes) => item.shelfId === shelf.id
              )?.name,
              id: item.id,
            }));
            setFunctionsForTable(newData);
            setFilteredFunctions(newData);
          })
          .catch((err) => toast.error(err));
      })
      .catch((err) => toast.error(err))
      .finally(() => {
        setIsLoading(false);
      });
  };
  const handleDelete = (functionData: TableDataTypes) => {
    const newData = filteredFunction.filter(
      (item) => item.id !== functionData.id
    );
    setFilteredFunctions(newData);
  };

  useEffect(() => {
    getData();
  }, []);

  const message =
    functionsForTable.length === 0
      ? 'No functions have been created yet'
      : 'Sorry, no matching results found';

  const handleOpenModal = () => {
    setOpenModal(true);
  };
  const handleModalClose = () => {
    setOpenModal(false);
  };

  const handleDeleteModalClose = () => {
    setOpenDeleteModal(false);
  };

  const handleEditModalClose = () => {
    setOpenEditModal(false);
  };

  const handleOpenDeleteModal = (data: TableDataTypes) => {
    setOpenDeleteModal(true);
    setSelectedFunction(data);
  };
  const handleOpenEditModal = (data: TableDataTypes) => {
    setSelectedFunction(data);
    setOpenEditModal(true);
  };

  const handleEdit = (functionData: TableDataTypes, newName: string) => {
    const newData = filteredFunction.map((item) => {
      if (item.id === functionData.id) {
        return { ...item, name: newName };
      }
      return item;
    });
    setFilteredFunctions(newData);
  };

  const actions: ActionType[] = [
    { comp: Delete, handler: handleOpenDeleteModal, key: 1 },
    { comp: Edit, handler: handleOpenEditModal, key: 2 },
  ];

  const headers = [
    { header: 'Function name', key: 'name' },
    {
      header: 'Binding shelf',
      key: 'shelfName',
    },
  ];
  if (isLoading) return null;
  return (
    <>
      {openModal && (
        <FunctionModal
          onCloseModal={handleModalClose}
          data={shelvesOptions}
          onGetData={getData}
        />
      )}
      {openDeleteModal && (
        <Modal title="Delete function" onCloseModal={handleDeleteModalClose}>
          <DeleteModal
            onDeleteFunction={handleDelete}
            onCloseModal={handleDeleteModalClose}
            functionData={selectedFunction}
          />
        </Modal>
      )}
      {openEditModal && (
        <Modal title="Rename function" onCloseModal={handleEditModalClose}>
          <FunctionEditModal
            onCloseModal={handleEditModalClose}
            functionData={selectedFunction}
            onGetData={getData}
            onEdit={handleEdit}
          />
        </Modal>
      )}
      <TableWrapper
        title="Functions"
        description="Create or use predifined function on your shleves"
      >
        <ActionsBox>
          <SearchBar
            placeholder="Search..."
            data={functionsForTable}
            setData={setFilteredFunctions}
            searchKey="name"
          />
          <ButtonActionsBox>
            <Button onClick={handleOpenModal} icon={<FaPlusCircle />}>
              Create function
            </Button>
          </ButtonActionsBox>
        </ActionsBox>
        {functionsForTable.length === 0 || filteredFunction.length === 0 ? (
          <Description>{message}</Description>
        ) : (
          <Table
            setTableData={setFilteredFunctions}
            data={filteredFunction}
            headers={headers}
            path="/dashboard/code/"
            actions={actions}
          />
        )}
      </TableWrapper>
    </>
  );
};

export default Functions;
