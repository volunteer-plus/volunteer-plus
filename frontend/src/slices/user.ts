import { sleep } from '@/helpers/common';
import { User } from '@/types/common';
import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import type { PayloadAction } from '@reduxjs/toolkit';

type LoginPayload = {
  email: string;
  password: string;
};

const login = createAsyncThunk(
  'user/login',
  async (payload: LoginPayload, thunkApi) => {
    await sleep(2000);
    thunkApi.dispatch(
      setUser({ email: payload.email, firstName: 'John', lastName: 'Doe' })
    );
  }
);

const logout = createAsyncThunk('user/logout', async (_, thunkApi) => {
  thunkApi.dispatch(clearUser());
});

const restoreSession = createAsyncThunk(
  'user/restoreSession',
  async (_, thunkApi) => {
    await sleep(2000);

    thunkApi.dispatch(stopLoading());
  }
);

interface UserState {
  user: User | null;
  isLoading: boolean;
}

const initialState: UserState = {
  user: null,
  isLoading: true,
};

const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    setUser: (state, action: PayloadAction<User>) => {
      state.user = action.payload;
    },
    clearUser: (state) => {
      state.user = null;
    },
    stopLoading: (state) => {
      state.isLoading = false;
    },
  },
});

const { setUser, clearUser, stopLoading } = userSlice.actions;

const userReducer = userSlice.reducer;

export { userReducer, login, logout, restoreSession, setUser, clearUser };
