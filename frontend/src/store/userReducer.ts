/* eslint-disable no-param-reassign */
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { UserType } from '../interfaces/dataTypes';
import { LocalStorage } from '../services/localStorage';

const localUser = LocalStorage.get('user')
  ? JSON.parse(LocalStorage.get('user') || '')
  : null;

export const initialUserState: { user: UserType | null } = {
  user: localUser || null,
};

const userSlice = createSlice({
  name: 'user',
  initialState: initialUserState,
  reducers: {
    setUser: (state, action: PayloadAction<UserType>) => {
      state.user = action.payload;
    },
    removeUser: (state) => {
      state.user = null;
    },
  },
});

export const { setUser, removeUser } = userSlice.actions;

export default userSlice.reducer;
