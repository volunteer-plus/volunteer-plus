import { AdminPageContent } from '@/components/admin';
import { Authenticated } from '@/components/auth';

const BareFundraisingActivitiesPage: React.FC = () => {
  return <AdminPageContent>Збори</AdminPageContent>;
};

const FundraisingActivitiesPage = Authenticated(BareFundraisingActivitiesPage);

export { FundraisingActivitiesPage };
