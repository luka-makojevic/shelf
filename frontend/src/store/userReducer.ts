/* eslint-disable no-param-reassign */
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { UserType } from '../utils/interfaces/dataTypes';

export const initialUserState: { user: UserType | null } = { user: null };

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
