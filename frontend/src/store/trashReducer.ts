/* eslint-disable no-param-reassign */
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { FileDataType } from '../interfaces/dataTypes';

export const initialTrashState: { trashData: FileDataType[] } = {
  trashData: [],
};

const trashSlice = createSlice({
  name: 'trash',
  initialState: initialTrashState,
  reducers: {
    setTrash: (state, action: PayloadAction<FileDataType[]>) => {
      state.trashData = action.payload;
    },
  },
});

export const { setTrash } = trashSlice.actions;

export default trashSlice.reducer;
