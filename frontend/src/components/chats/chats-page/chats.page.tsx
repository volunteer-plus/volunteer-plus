import { AdminPageContent } from '@/components/admin';
import { Authenticated } from '@/components/auth';

const BareChatsPage: React.FC = () => {
  return <AdminPageContent>Chats</AdminPageContent>;
};

const ChatsPage = Authenticated(BareChatsPage);

export { ChatsPage };
