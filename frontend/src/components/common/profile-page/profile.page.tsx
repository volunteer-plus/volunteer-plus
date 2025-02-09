import { AdminPageContent } from '@/components/admin';
import { Authenticated } from '@/components/auth';

const BareProfilePage: React.FC = () => {
  return <AdminPageContent>Profile</AdminPageContent>;
};

const ProfilePage = Authenticated(BareProfilePage);

export { ProfilePage };
