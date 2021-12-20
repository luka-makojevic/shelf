import { configureStore } from '@reduxjs/toolkit';
import loadingReducer from './loadingReducer';
import userReducer from './userReducer';
import shelfReducer from './shelfReducer';
import fileReducer from './fileReducer';
import pathHistoryReducer from './pathHistory';
import sharedReducer from './sharedReducer';

export const store = configureStore({
  reducer: {
    user: userReducer,
    loading: loadingReducer,
    shelf: shelfReducer,
    file: fileReducer,
    shared: sharedReducer,
    pathHistory: pathHistoryReducer,
  },
});
export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof store.getState>;
