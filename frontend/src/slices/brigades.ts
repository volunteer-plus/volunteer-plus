import { makeLoadedListSlice } from '@/helpers/slices';
import { brigadesService } from '@/services/brigades/brigades';

const { brigadesReducer, clearBrigades, loadBrigades, reloadBrigades } =
  makeLoadedListSlice({
    name: 'brigades',
    load: brigadesService.getBrigades.bind(brigadesService),
  });

export { brigadesReducer, clearBrigades, loadBrigades, reloadBrigades };
