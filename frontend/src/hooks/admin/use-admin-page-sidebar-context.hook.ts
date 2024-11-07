import { AdminPageSidebarContext } from '@/contexts';
import { useContext } from 'react';

function useAdminPageSidebarContext() {
  return useContext(AdminPageSidebarContext);
}

export { useAdminPageSidebarContext };
