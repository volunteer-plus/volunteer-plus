import { Navigate } from 'react-router-dom';
import { FullscreenLoader } from '@/components/common';
import { useUser } from '@/hooks/auth';
import { User } from '@/types/common';
import {
  isBrigadeAdmin,
  isServiceman,
  isSuperAdmin,
  isVolunteer,
} from '@/helpers/user';

interface AllowedRoles {
  brigadeAdmin: boolean;
  superAdmin: boolean;
  serviceman: boolean;
  volunteer: boolean;
}

interface Options {
  allowedRoles: Partial<AllowedRoles>;
}

function checkAccessByAllowedRoles(
  user: User,
  allowedRoles?: Partial<AllowedRoles>
): boolean {
  if (!allowedRoles) {
    return true;
  }

  if (isSuperAdmin(user) && allowedRoles.superAdmin) {
    return true;
  }

  if (isBrigadeAdmin(user) && allowedRoles.brigadeAdmin) {
    return true;
  }

  if (isServiceman(user) && allowedRoles.serviceman) {
    return true;
  }

  if (isVolunteer(user) && allowedRoles.volunteer) {
    return true;
  }

  return false;
}

const Authenticated = (Component: React.FC, options?: Options) => {
  const WrappedComponent: React.FC = () => {
    const { user, isLoading } = useUser();

    if (isLoading) {
      return <FullscreenLoader />;
    }

    if (!user) {
      return <Navigate to='/sign-in' />;
    }

    if (!checkAccessByAllowedRoles(user, options?.allowedRoles)) {
      throw new Response(undefined, { status: 404 });
    }

    return <Component />;
  };

  return WrappedComponent;
};

export { Authenticated };
