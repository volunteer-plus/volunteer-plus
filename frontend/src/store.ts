import { configureStore } from '@reduxjs/toolkit';
import { userReducer } from './slices/user';
import { brigadesReducer } from './slices/brigades';

const store = configureStore({
  reducer: {
    user: userReducer,
    brigades: brigadesReducer,
  },
});

export { store };
