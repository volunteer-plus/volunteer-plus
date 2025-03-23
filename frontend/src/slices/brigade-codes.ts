import { makeLoadedSlice } from '@/helpers/slices';
import { brigadesService } from '@/services/brigades/brigades';

const {
  brigadeCodesReducer,
  clearBrigadeCodes,
  loadBrigadeCodes,
  loadBrigadeCodesIfNotLoaded,
  reloadBrigadeCodes,
} = makeLoadedSlice({
  name: 'brigadeCodes',
  load: brigadesService.getBrigadeCodes.bind(brigadesService),
});

export {
  brigadeCodesReducer,
  clearBrigadeCodes,
  loadBrigadeCodes,
  loadBrigadeCodesIfNotLoaded,
  reloadBrigadeCodes,
};
