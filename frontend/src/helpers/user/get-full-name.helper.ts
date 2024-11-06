import { User } from '@/types/common';

function getFullName(user: User): string {
  return `${user.firstName} ${user.lastName}`;
}

export { getFullName };
