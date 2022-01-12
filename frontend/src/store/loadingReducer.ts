/* eslint-disable no-param-reassign */
import { createSlice, PayloadAction } from '@reduxjs/toolkit';

export const initialLoadingState: { loading: boolean } = {
  loading: false,
};

const loadingSlice = createSlice({
  name: 'loading',
  initialState: initialLoadingState,
  reducers: {
    setIsLoading: (state, action: PayloadAction<boolean>) => {
      state.loading = action.payload;
    },
  },
});

export const { setIsLoading } = loadingSlice.actions;

export default loadingSlice.reducer;
