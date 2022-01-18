import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
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
  const [deletedModalOpen, setDeleteModalOpen] = useState<boolean>();
  const [selectedFunction, setSelectedFunction] = useState<TableDataTypes>();
  const [shelvesOptions, setShelvesOptions] = useState<ShelvesOptionTypes[]>(
    []
  );
  const [openEditModal, setOpenEditModal] = useState(false);
  const [functionForTable, setFunctionsForTable] = useState<TableDataTypes[]>(
    []
  );

  const getData = () => {
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
      .finally(() => {});
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
  const handleOpenDeleteModal = (data: TableDataTypes) => {
    setDeleteModalOpen(true);
    setSelectedFunction(data);
  };
  const handleOpenEditModal = (data: TableDataTypes) => {
    setSelectedFunction(data);
    setOpenEditModal(true);
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

  return (
    <>
      {openModal && (
        <FunctionModal
          onCloseModal={handleModalClose}
          data={shelvesOptions}
          onGetData={getData}
        />
      )}
      <TableWrapper
        title="Functions"
        description="Create or use predifined function on your shleves"
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
