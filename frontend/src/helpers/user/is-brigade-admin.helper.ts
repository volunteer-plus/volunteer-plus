import { UserRole } from '@/enums/common';
import { User } from '@/types/common';

const isBrigadeAdmin = (user: Pick<User, 'role'>): boolean => {
  return user.role === UserRole.BRIGADE_ADMIN;
};

export { isBrigadeAdmin };
