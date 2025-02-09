import { User } from '@/types/common';
import { isSuperAdmin } from './is-super-admin.helper';
import { isBrigadeAdmin } from './is-brigade-admin.helper';
import { isServiceman } from './is-serviceman.helper';
import { isVolunteer } from './is-volunteer.helper';

function getUserHomepage(user: User): string {
  if (isSuperAdmin(user)) {
    return '/brigades';
  } else if (isBrigadeAdmin(user)) {
    return '/my-brigade';
  } else if (isServiceman(user)) {
    return '/serviceman/requests';
  } else if (isVolunteer(user)) {
    return '/volunteer/requests';
  }

  return '/';
}

export { getUserHomepage };
