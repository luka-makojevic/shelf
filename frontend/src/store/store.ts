import { configureStore } from '@reduxjs/toolkit';
import loadingReducer from './loadingReducer';
import userReducer from './userReducer';
import shelfReducer from './shelfReducer';
import trashReducer from './trashReducer';
import fileReducer from './fileReducer';
import pathHistoryReducer from './pathHistory';

export const store = configureStore({
  reducer: {
    user: userReducer,
    loading: loadingReducer,
    shelf: shelfReducer,
    trash: trashReducer,
    file: fileReducer,
    pathHistory: pathHistoryReducer,
  },
});
export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof store.getState>;
