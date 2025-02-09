import { AdminPageContent } from '@/components/admin';
import { Authenticated } from '@/components/auth';

const BareServicemanRequestsPage: React.FC = () => {
  return <AdminPageContent>Serviceman requests</AdminPageContent>;
};

const ServicemanRequestsPage = Authenticated(BareServicemanRequestsPage, {
  allowedRoles: {
    serviceman: true,
  },
});

export { ServicemanRequestsPage };
