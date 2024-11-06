import { Navigate } from 'react-router-dom';
import { FullscreenLoader } from '@/components/common';
import { useUser } from '@/hooks/auth';
import { User } from '@/types/common';

interface Options {
  getRedirectUrl?: (user: User) => string;
}

const PublicOnly = (
  Component: React.FC,
  { getRedirectUrl = () => '/' }: Options
) => {
  const WrappedComponent: React.FC = () => {
    const { user, isLoading } = useUser();

    if (isLoading) {
      return <FullscreenLoader />;
    }

    if (user) {
      return <Navigate to={getRedirectUrl(user)} />;
    }

    return <Component />;
  };

  return WrappedComponent;
};

export { PublicOnly };
