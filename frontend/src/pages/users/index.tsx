import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { Table } from '../../components/table/table';
import { TableDataTypes, UserDataType } from '../../interfaces/dataTypes';
import Modal from '../../components/modal';
import { Description } from '../../components/text/text-styles';
import SearchBar from '../../components/UI/searchBar/searchBar';
import { ActionsBox } from '../../components/table/tableWrapper.styles';
import TableWrapper from '../../components/table/TableWrapper';
import {
  ActionType,
  Delete,
  Edit,
} from '../../components/table/table.interfaces';
import DeleteUserModal from '../../components/modal/deleteUserModal';
import ChangeUserRoleModal from '../../components/modal/changeUserRoleModal';
import adminServices from '../../services/adminServices';

const headers = [
  { header: 'Name', key: 'name' },
  { header: 'Email', key: 'email' },
  { header: 'Role', key: 'role' },
];

const Users = () => {
  const [users, setUsers] = useState<UserDataType[]>([]);
  const [selectedUser, setSelectedUser] = useState<TableDataTypes | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [usersForTable, setUsersForTable] = useState<TableDataTypes[]>([]);
  const [filteredUsers, setFilteredUsers] = useState<TableDataTypes[]>([]);

  const [deleteModalOpen, setDeleteModalOpen] = useState<boolean>(false);
  const [editModalOpen, setEditModalOpen] = useState<boolean>(false);

  const getData = () => {
    setIsLoading(true);
    adminServices
      .getUsers()
      .then((res) => {
        setUsers(res.data.users);
      })
      .catch((err) => toast.error(err))
      .finally(() => {
        setIsLoading(false);
      });
  };

  useEffect(() => {
    getData();
  }, []);

  useEffect(() => {
    const newUsers = users.map((user) => ({
      name: `${user.firstName} ${user.lastName}`,
      email: user.email,
      role: user.role.name,
      id: user.id,
    }));

    setUsersForTable(newUsers);
    setFilteredUsers(newUsers);
  }, [users]);

  const handleModalClose = () => {
    setDeleteModalOpen(false);
    setEditModalOpen(false);
    setSelectedUser(null);
  };

  const handleOpenDeleteModal = (user: TableDataTypes) => {
    setDeleteModalOpen(true);
    setSelectedUser(user);
  };

  const handleOpenEditModal = (user: TableDataTypes) => {
    setEditModalOpen(true);
    setSelectedUser(user);
  };

  const handleDelete = () => {
    getData();
  };

  const handleRoleChange = () => {
    getData();
  };

  const actions: ActionType[] = [
    { comp: Delete, handler: handleOpenDeleteModal, key: 1 },
    { comp: Edit, handler: handleOpenEditModal, key: 2 },
  ];

  const message =
    users.length === 0
      ? 'No users have been registered yet'
      : 'Sorry, no matching results found';

  if (isLoading) return null;

  return (
    <>
      {deleteModalOpen && (
        <Modal title="Delete user" onCloseModal={handleModalClose}>
          <DeleteUserModal
            onDeleteUser={handleDelete}
            onCloseModal={handleModalClose}
            user={selectedUser}
          />
        </Modal>
      )}
      {editModalOpen && (
        <Modal title="Change role" onCloseModal={handleModalClose}>
          <ChangeUserRoleModal
            onRoleChange={handleRoleChange}
            onCloseModal={handleModalClose}
            user={selectedUser}
          />
        </Modal>
      )}
      <TableWrapper title="Users" description="Manage users.">
        <ActionsBox>
          <SearchBar
            placeholder="Search..."
            data={usersForTable}
            setData={setFilteredUsers}
            searchKey="name"
          />
        </ActionsBox>
        {users.length === 0 || filteredUsers.length === 0 ? (
          <Description>{message}</Description>
        ) : (
          <Table
            setTableData={setFilteredUsers}
            data={filteredUsers}
            headers={headers}
            path="/"
            actions={actions}
          />
        )}
      </TableWrapper>
    </>
  );
};

export default Users;
