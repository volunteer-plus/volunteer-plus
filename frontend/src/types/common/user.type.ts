import { UserRole } from '@/enums/common';

interface User {
  id: number;
  firstName: string;
  middleName: string;
  lastName: string;
  email: string;
  role: UserRole;
  dateOfBirth: string | null;
  logoS3Link: string | null;
  logoFilename: string | null;
  phoneNumber: string | null;
}

export type { User };
