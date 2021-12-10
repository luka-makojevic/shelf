import { configureStore } from '@reduxjs/toolkit';
import loadingReducer from './loadingReducer';
import userReducer from './userReducer';

export const store = configureStore({
  reducer: {
    user: userReducer,
    loading: loadingReducer
  },
});
export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof store.getState>;
