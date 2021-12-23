/* eslint-disable no-param-reassign */
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { PathHistoryData } from '../interfaces/dataTypes';

export const initialPathHistory: { pathHistory: PathHistoryData[] } = {
  pathHistory: [],
};

const pathHistorySlice = createSlice({
  name: 'pathHistory',
  initialState: initialPathHistory,
  reducers: {
    setPathHistory: (state, action: PayloadAction<PathHistoryData[]>) => {
      state.pathHistory = action.payload;
    },
  },
});

export const { setPathHistory } = pathHistorySlice.actions;

export default pathHistorySlice.reducer;
