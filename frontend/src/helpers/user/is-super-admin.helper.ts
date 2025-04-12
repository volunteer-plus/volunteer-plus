import { UserRole } from '@/enums/common';
import { User } from '@/types/common';

const isSuperAdmin = (user: Pick<User, 'role'>): boolean => {
  return user.role === UserRole.SUPER_ADMIN;
};

export { isSuperAdmin };
