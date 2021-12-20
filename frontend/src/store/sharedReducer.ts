/* eslint-disable no-param-reassign */
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { FileDataType } from '../interfaces/dataTypes';

export const initialSharedState: { data: FileDataType[] } = {
  data: [],
};

const sharedSlice = createSlice({
  name: 'shared',
  initialState: initialSharedState,
  reducers: {
    setShared: (state, action: PayloadAction<FileDataType[]>) => {
      state.data = action.payload;
    },
  },
});

export const { setShared } = sharedSlice.actions;

export default sharedSlice.reducer;
