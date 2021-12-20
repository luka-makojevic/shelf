/* eslint-disable no-param-reassign */
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { FileDataType } from '../interfaces/dataTypes';

export const initialFileState: { files: FileDataType[] } = {
  files: [],
};

const fileSlice = createSlice({
  name: 'file',
  initialState: initialFileState,
  reducers: {
    setFiles: (state, action: PayloadAction<FileDataType[]>) => {
      state.files = action.payload;
    },
  },
});

export const { setFiles } = fileSlice.actions;

export default fileSlice.reducer;
