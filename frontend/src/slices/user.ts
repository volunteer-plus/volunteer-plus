import { accessTokenService } from '@/services/common/access-token';
import { authService, InvalidCredentialsError } from '@/services/common/auth';
import { userService } from '@/services/common/user/user.service';
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
    thunkApi.dispatch(setIsLoginFailed(false));

    try {
      const { token } = await authService.login(payload);

      accessTokenService.set(token);

      await thunkApi.dispatch(restoreSession());
    } catch (error) {
      if (error instanceof InvalidCredentialsError) {
        thunkApi.dispatch(setIsLoginFailed(true));
        thunkApi.dispatch(setIsLoading(false));
      }

      throw error;
    }
  }
);

const logout = createAsyncThunk('user/logout', async (_, thunkApi) => {
  accessTokenService.clear();
  thunkApi.dispatch(clearUser());
});

const restoreSession = createAsyncThunk(
  'user/restoreSession',
  async (_, thunkApi) => {
    thunkApi.dispatch(setIsLoading(true));
    thunkApi.dispatch(setIsLoginFailed(false));

    const token = accessTokenService.get();

    if (!token || accessTokenService.isTokenExpired(token)) {
      thunkApi.dispatch(setIsLoading(false));
      return;
    }

    const user = await userService.getMe();

    thunkApi.dispatch(setUser(user));
    thunkApi.dispatch(setIsLoading(false));
  }
);

interface UserState {
  user: User | null;
  isLoading: boolean;
  isLoginFailed: boolean;
}

const initialState: UserState = {
  user: null,
  isLoading: true,
  isLoginFailed: false,
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
    setIsLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoading = action.payload;
    },
    setIsLoginFailed: (state, action: PayloadAction<boolean>) => {
      state.isLoginFailed = action.payload;
    },
  },
});

const { setUser, clearUser, setIsLoading, setIsLoginFailed } =
  userSlice.actions;

const userReducer = userSlice.reducer;

export { userReducer, login, logout, restoreSession, setUser, clearUser };
