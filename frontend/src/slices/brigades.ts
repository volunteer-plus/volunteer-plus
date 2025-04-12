import { makeLoadedSlice } from '@/helpers/slices';
import { brigadesService } from '@/services/brigades/brigades';

const { brigadesReducer, clearBrigades, loadBrigades, reloadBrigades } =
  makeLoadedSlice({
    name: 'brigades',
    load: brigadesService.getBrigades.bind(brigadesService),
  });

export { brigadesReducer, clearBrigades, loadBrigades, reloadBrigades };
