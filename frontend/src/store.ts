import { configureStore } from '@reduxjs/toolkit';

import { userReducer } from './slices/user';
import { brigadesReducer } from './slices/brigades';
import { brigadeCodesReducer } from './slices/brigade-codes';
import { myBrigadeReducer } from './slices/my-brigade';
import { myBrigadeInvitesReducer } from './slices/my-brigade-invites';

const store = configureStore({
  reducer: {
    user: userReducer,
    brigades: brigadesReducer,
    brigadeCodes: brigadeCodesReducer,
    myBrigade: myBrigadeReducer,
    myBrigadeInvites: myBrigadeInvitesReducer,
  },
});

export { store };
