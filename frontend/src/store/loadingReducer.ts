/* eslint-disable no-param-reassign */
import { createSlice } from '@reduxjs/toolkit';

export const initialLoadingState: { loadingCounter: number; loading: boolean } =
  {
    loadingCounter: 0,
    loading: false,
  };

const loadingSlice = createSlice({
  name: 'loading',
  initialState: initialLoadingState,
  reducers: {
    increaseLoadingCounter: (state) => {
      state.loadingCounter += 1;
      if (state.loadingCounter) {
        state.loading = true;
      }
    },
    decreaseLoadingCounter: (state) => {
      state.loadingCounter -= 1;
      if (state.loadingCounter === 0) {
        state.loading = false;
      }
    },
  },
});

export const { increaseLoadingCounter, decreaseLoadingCounter } =
  loadingSlice.actions;

export default loadingSlice.reducer;
