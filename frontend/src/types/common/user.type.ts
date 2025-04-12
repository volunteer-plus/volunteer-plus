import { UserRole } from '@/enums/common';

interface User {
  firstName: string;
  lastName: string;
  email: string;
  role: UserRole;
}

export type { User };
