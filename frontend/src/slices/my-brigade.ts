import { makeLoadedSlice } from '@/helpers/slices';
import { brigadesService } from '@/services/brigades/brigades';

const {
  clearMyBrigade,
  loadMyBrigade,
  loadMyBrigadeIfNotLoaded,
  myBrigadeReducer,
  reloadMyBrigade,
} = makeLoadedSlice({
  name: 'myBrigade',
  load: brigadesService.getMyBrigade.bind(brigadesService),
});

export {
  clearMyBrigade,
  loadMyBrigade,
  loadMyBrigadeIfNotLoaded,
  myBrigadeReducer,
  reloadMyBrigade,
};
