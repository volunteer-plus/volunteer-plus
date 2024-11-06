import { User } from '@/types/common';

const isBrigadeAdmin = (user: User): boolean => {
  return user.email === 'brigade.admin@example.com';
};

export { isBrigadeAdmin };
