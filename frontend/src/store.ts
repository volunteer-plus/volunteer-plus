import { configureStore } from '@reduxjs/toolkit';

import { userReducer } from './slices/user';
import { brigadesReducer } from './slices/brigades';
import { brigadeCodesReducer } from './slices/brigade-codes';
import { myBrigadeReducer } from './slices/my-brigade';

const store = configureStore({
  reducer: {
    user: userReducer,
    brigades: brigadesReducer,
    brigadeCodes: brigadeCodesReducer,
    myBrigade: myBrigadeReducer,
  },
});

export { store };
