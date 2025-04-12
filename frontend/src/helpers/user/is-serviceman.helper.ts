import { UserRole } from '@/enums/common';
import { User } from '@/types/common';

const isServiceman = (user: Pick<User, 'role'>): boolean => {
  return user.role === UserRole.SERVICEMAN;
};

export { isServiceman };
