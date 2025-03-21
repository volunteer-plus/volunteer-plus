import {
  createAsyncThunk,
  createSlice,
  Draft,
  PayloadAction,
} from '@reduxjs/toolkit';
import { capitalize } from 'lodash';

interface MakeLoadedListSliceOptions<R, O, N extends string> {
  name: N;
  load: (options: O) => Promise<R[]>;
}

interface LoadedListState<R, O> {
  data: R[];
  isLoading: boolean;
  lastLoadOptions: O | null;
  isLoaded: boolean;
}

function makeLoadedListSlice<R, O, N extends string>({
  name,
  load,
}: MakeLoadedListSliceOptions<R, O, N>) {
  type State = LoadedListState<R, O>;

  const initialState: State = {
    data: [],
    isLoading: true,
    lastLoadOptions: null,
    isLoaded: false,
  };

  const loadThunk = createAsyncThunk(
    `${name}/load`,
    async (options: O, thunkApi) => {
      thunkApi.dispatch(slice.actions.setIsLoading(true));

      const loadedData = await load(options);

      thunkApi.dispatch(slice.actions.setData(loadedData));
      thunkApi.dispatch(slice.actions.setIsLoading(false));
      thunkApi.dispatch(slice.actions.setIsLoaded(true));
    }
  );

  const reloadThunk = createAsyncThunk(
    `${name}/reload`,
    async (_, thunkApi) => {
      const lastOptions = (thunkApi.getState() as State).lastLoadOptions;

      if (!lastOptions) {
        return;
      }

      await thunkApi.dispatch(loadThunk(lastOptions));
    }
  );

  const slice = createSlice({
    initialState,
    name,
    reducers: {
      setData: (state, action: PayloadAction<R[]>) => {
        state.data = action.payload as Draft<R[]>;
      },
      clear: (state) => {
        state.data = [];
        state.isLoading = false;
        state.lastLoadOptions = null;
      },
      setIsLoading: (state, action: PayloadAction<boolean>) => {
        state.isLoading = action.payload;
      },
      setIsLoaded: (state, action: PayloadAction<boolean>) => {
        state.isLoaded = action.payload;
      },
    },
  });

  type ActionsCreatorsAndReducer = Record<
    `load${Capitalize<N>}`,
    typeof loadThunk
  > &
    Record<`reload${Capitalize<N>}`, typeof reloadThunk> &
    Record<`clear${Capitalize<N>}`, typeof slice.actions.clear> &
    Record<`${N}Reducer`, typeof slice.reducer>;

  return {
    [`load${capitalize(name)}`]: loadThunk,
    [`reload${capitalize(name)}`]: reloadThunk,
    [`clear${capitalize(name)}`]: slice.actions.clear,
    [`${name}Reducer`]: slice.reducer,
  } as ActionsCreatorsAndReducer;
}

export { makeLoadedListSlice };
