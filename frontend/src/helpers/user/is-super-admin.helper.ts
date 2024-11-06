import { User } from '@/types/common';

const isSuperAdmin = (user: User): boolean => {
  return user.email === 'super.admin@example.com';
};

export { isSuperAdmin };
