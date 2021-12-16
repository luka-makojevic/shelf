/* eslint-disable no-param-reassign */
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { ShelfDataType } from '../interfaces/dataTypes';

export const initialShelfState: { shelves: ShelfDataType[] } = {
  shelves: [],
};

const shelfSlice = createSlice({
  name: 'shelf',
  initialState: initialShelfState,
  reducers: {
    setShelves: (state, action: PayloadAction<ShelfDataType[]>) => {
      state.shelves = action.payload;
    },
  },
});

export const { setShelves } = shelfSlice.actions;

export default shelfSlice.reducer;
