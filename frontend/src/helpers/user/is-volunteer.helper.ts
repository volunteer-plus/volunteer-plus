import { User } from '@/types/common';

const isVolunteer = (user: User): boolean => {
  return user.email === 'volunteer@example.com';
};

export { isVolunteer };
