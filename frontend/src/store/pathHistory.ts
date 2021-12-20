/* eslint-disable no-param-reassign */
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { PathHistoryDataType } from '../interfaces/dataTypes';

export const initialPathHistory: { pathHistory: PathHistoryDataType[] } = {
  pathHistory: [],
};

const pathHistorySlice = createSlice({
  name: 'pathHistory',
  initialState: initialPathHistory,
  reducers: {
    setPathHistory: (state, action: PayloadAction<PathHistoryDataType[]>) => {
      state.pathHistory = action.payload;
    },
  },
});

export const { setPathHistory } = pathHistorySlice.actions;

export default pathHistorySlice.reducer;
