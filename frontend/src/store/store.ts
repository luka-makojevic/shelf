import { configureStore } from '@reduxjs/toolkit';
import loadingReducer from './loadingReducer';
import userReducer from './userReducer';
import shelfReducer from './shelfReducer';
import trashReducer from './trashReducer';

export const store = configureStore({
  reducer: {
    user: userReducer,
    loading: loadingReducer,
    shelf: shelfReducer,
    trash: trashReducer,
  },
});
export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof store.getState>;
