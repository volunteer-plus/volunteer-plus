import { makeLoadedSlice } from '@/helpers/slices';
import { brigadesService } from '@/services/brigades/brigades';

const {
  clearMyBrigadeInvites,
  loadMyBrigadeInvites,
  loadMyBrigadeInvitesIfNotLoaded,
  myBrigadeInvitesReducer,
  reloadMyBrigadeInvites,
} = makeLoadedSlice({
  name: 'myBrigadeInvites',
  load: brigadesService.getBrigadeInvites.bind(brigadesService),
});

export {
  clearMyBrigadeInvites,
  loadMyBrigadeInvites,
  loadMyBrigadeInvitesIfNotLoaded,
  myBrigadeInvitesReducer,
  reloadMyBrigadeInvites,
};
