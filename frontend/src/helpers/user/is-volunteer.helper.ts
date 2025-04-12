import { UserRole } from '@/enums/common';
import { User } from '@/types/common';

const isVolunteer = (user: Pick<User, 'role'>): boolean => {
  return user.role === UserRole.VOLUNTEER;
};

export { isVolunteer };
