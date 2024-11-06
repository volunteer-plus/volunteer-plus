import { User } from '@/types/common';
import { isSuperAdmin } from './is-super-admin.helper';
import { isBrigadeAdmin } from './is-brigade-admin.helper';

function getUserHomepage(user: User): string {
  if (isSuperAdmin(user)) {
    return '/brigades';
  } else if (isBrigadeAdmin(user)) {
    return '/brigade-users';
  }

  return '/';
}

export { getUserHomepage };
