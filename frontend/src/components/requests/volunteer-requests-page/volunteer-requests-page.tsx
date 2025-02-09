import { AdminPageContent } from '@/components/admin';
import { Authenticated } from '@/components/auth';

const BareVolunteerRequestsPage: React.FC = () => {
  return <AdminPageContent>Volunteer requests</AdminPageContent>;
};

const VolunteerRequestsPage = Authenticated(BareVolunteerRequestsPage, {
  allowedRoles: {
    volunteer: true,
  },
});

export { VolunteerRequestsPage };
