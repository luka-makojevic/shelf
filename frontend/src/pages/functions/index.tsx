import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import DeleteModal from '../../components/modal/deleteModal';
import FunctionModal from '../../components/modal/functionModal';
import { ModifyModal } from '../../components/modal/modifyModal';
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
import { ShelfFormData, TableDataTypes } from '../../interfaces/dataTypes';
import functionService from '../../services/functionService';
import shelfServices from '../../services/shelfServices';
import { eventTriggerOptions } from '../../utils/fixtures/functionOptions';

const headers = [
  { header: 'Function name', key: 'name' },
  {
    header: 'Binding shelf',
    key: 'shelfName',
  },
  {
    header: 'Language',
    key: 'language',
  },
  {
    header: 'Trigger',
    key: 'trigger',
  },
];

const Functions = () => {
  const [filteredFunction, setFilteredFunctions] = useState<TableDataTypes[]>(
    []
  );
  const [openModal, setOpenModal] = useState(false);
  const [selectedFunction, setSelectedFunction] =
    useState<TableDataTypes | null>();
  const [shelvesOptions, setShelvesOptions] = useState<ShelvesOptionTypes[]>(
    []
  );
  const [openEditModal, setOpenEditModal] = useState(false);
  const [functionForTable, setFunctionsForTable] = useState<TableDataTypes[]>(
    []
  );
  const [openDeleteModal, setOpenDeleteModal] = useState(false);
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
              custom: item.custom ? 1 : 0,
              language: item.language,
              id: item.id,
              trigger: eventTriggerOptions.find(
                (event) => item.eventId === Number(event.value)
              )?.text,
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

  useEffect(() => {
    getData();
  }, []);

  const message =
    functionForTable.length === 0
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
    setSelectedFunction(null);
  };

  const handleOpenDeleteModal = (data: TableDataTypes) => {
    setOpenDeleteModal(true);
    setSelectedFunction(data);
  };
  const handleOpenEditModal = (data: TableDataTypes) => {
    setSelectedFunction(data);
    setOpenEditModal(true);
  };

  const handleEdit = (newName: string) => {
    const newData = filteredFunction.map((item) => {
      if (item.id === selectedFunction?.id) {
        return { ...item, name: newName };
      }
      return item;
    });
    setFilteredFunctions(newData);
  };

  const handleDelete = () => {
    if (selectedFunction)
      functionService
        .deleteFunction(selectedFunction?.id)
        .then(() => toast.success('Function successfully deleted'))
        .catch((err) => toast.error(err.response?.data?.message));
    const newData = filteredFunction.filter(
      (item) => item.id !== selectedFunction?.id
    );
    setFilteredFunctions(newData);
    handleDeleteModalClose();
  };

  const actions: ActionType[] = [
    { comp: Delete, handler: handleOpenDeleteModal, key: 1 },
    { comp: Edit, handler: handleOpenEditModal, key: 2 },
  ];

  const onSubmit = (data: ShelfFormData) => {
    const functionName = data.name;
    if (selectedFunction) {
      const payload = {
        functionId: selectedFunction.id,
        newName: functionName,
      };
      functionService
        .renameFunction(payload)
        .then((res) => {
          toast.success(res.data.message);
          handleEdit(functionName);
        })
        .catch((err) => {
          toast.error(err.response?.data.message);
        });
    }
    handleEditModalClose();
  };

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
        <DeleteModal
          title="Delete function"
          message={`Are you sure you want to delete '${selectedFunction?.name}'? This action will permanently delete this function!`}
          onDelete={handleDelete}
          onCloseModal={handleDeleteModalClose}
        />
      )}
      {openEditModal && (
        <ModifyModal
          title="Edit name"
          onCloseModal={handleEditModalClose}
          onSubmit={onSubmit}
          buttonMessage="Rename "
          placeHolder="Edit name"
          defaultValue={selectedFunction?.name}
        />
      )}
      <TableWrapper
        title="Functions"
        description="Create or use predefined function on your shelves"
      >
        <ActionsBox>
          <SearchBar
            placeholder="Search..."
            data={functionForTable}
            setData={setFilteredFunctions}
            searchKey="name"
          />
          <ButtonActionsBox>
            <Button onClick={handleOpenModal}>Create function</Button>
          </ButtonActionsBox>
        </ActionsBox>
        {functionForTable.length === 0 || filteredFunction.length === 0 ? (
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
