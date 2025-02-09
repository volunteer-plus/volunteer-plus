import { User } from '@/types/common';

const isServiceman = (user: User): boolean => {
  return user.email === 'serviceman@example.com';
};

export { isServiceman };
