import {
  createAsyncThunk,
  createSlice,
  Draft,
  PayloadAction,
} from '@reduxjs/toolkit';

interface MakeLoadedListSliceOptions<R, O, N extends string> {
  name: N;
  load: (options: O) => Promise<R>;
}

interface LoadedListState<D, O> {
  data: { value: D } | null;
  isLoading: boolean;
  lastLoadOptions: {
    value: O;
  } | null;
}

function capitalizeName<T extends string>(target: T): Capitalize<T> {
  return (target.charAt(0).toUpperCase() + target.slice(1)) as Capitalize<T>;
}

function makeLoadedSlice<D, N extends string, O = void>({
  name,
  load,
}: MakeLoadedListSliceOptions<D, O, N>) {
  type State = LoadedListState<D, O>;

  const initialState: State = {
    data: null,
    isLoading: false,
    lastLoadOptions: null,
  };

  const loadThunk = createAsyncThunk(
    `${name}/load`,
    async (options: O, thunkApi) => {
      thunkApi.dispatch(slice.actions.setIsLoading(true));

      const loadedData = await load(options);

      thunkApi.dispatch(
        slice.actions.processLoadedData({
          data: loadedData,
          options,
        })
      );
    }
  );

  const loadIfNotLoadedThunk = createAsyncThunk(
    `${name}/loadIfNotLoaded`,
    async (options: O, thunkApi) => {
      const { data, isLoading } = (
        thunkApi.getState() as Record<string, unknown>
      )[slice.reducerPath] as State;

      if (data || isLoading) {
        return;
      }

      await thunkApi.dispatch(loadThunk(options));
    }
  );

  const reloadThunk = createAsyncThunk(
    `${name}/reload`,
    async (_, thunkApi) => {
      const lastOptions = (
        (thunkApi.getState() as Record<string, unknown>)[
          slice.reducerPath
        ] as State
      ).lastLoadOptions;

      if (!lastOptions) {
        return;
      }

      await thunkApi.dispatch(loadThunk(lastOptions.value));
    }
  );

  const slice = createSlice({
    initialState,
    name,
    reducers: {
      processLoadedData: (
        state,
        action: PayloadAction<{
          data: D;
          options: O;
        }>
      ) => {
        state.data = {
          value: action.payload.data as Draft<D>,
        };
        state.lastLoadOptions = {
          value: action.payload.options as Draft<O>,
        };
        state.isLoading = false;
      },
      clear: (state) => {
        state.data = null;
        state.isLoading = false;
        state.lastLoadOptions = null;
      },
      setIsLoading: (state, action: PayloadAction<boolean>) => {
        state.isLoading = action.payload;
      },
    },
  });

  type ActionsCreatorsAndReducer = Record<
    `load${Capitalize<N>}`,
    typeof loadThunk
  > &
    Record<`load${Capitalize<N>}IfNotLoaded`, typeof loadIfNotLoadedThunk> &
    Record<`reload${Capitalize<N>}`, typeof reloadThunk> &
    Record<`clear${Capitalize<N>}`, typeof slice.actions.clear> &
    Record<`${N}Reducer`, typeof slice.reducer>;

  return {
    [`load${capitalizeName(name)}`]: loadThunk,
    [`load${capitalizeName(name)}IfNotLoaded`]: loadIfNotLoadedThunk,
    [`reload${capitalizeName(name)}`]: reloadThunk,
    [`clear${capitalizeName(name)}`]: slice.actions.clear,
    [`${name}Reducer`]: slice.reducer,
  } as ActionsCreatorsAndReducer;
}

export { makeLoadedSlice };
