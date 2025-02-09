import classNames from 'classnames';
import { useMemo } from 'react';

import { useUser } from '@/hooks/auth';
import { SidebarService } from './services';

import styles from './styles.module.scss';
import { SidebarItem } from './components';
import { useAdminPageSidebarContext } from '@/hooks/admin';

const AdminPageSidebar: React.FC = () => {
  const { user } = useUser();
  const { isOpen } = useAdminPageSidebarContext();

  const sidebarService = useMemo(() => {
    if (!user) {
      return null;
    }

    return new SidebarService({ user });
  }, [user]);

  const itemsConfigs = useMemo(() => {
    if (!sidebarService) {
      return [];
    }

    return sidebarService.getItemsConfigs();
  }, [sidebarService]);

  return (
    <aside
      className={classNames(styles.sidebar, {
        [styles.expanded]: isOpen,
      })}
    >
      <div className={styles.main}>
        <nav className={styles.itemsList}>
          {itemsConfigs.map((config) => (
            <SidebarItem key={config.key} config={config} />
          ))}
        </nav>
      </div>
    </aside>
  );
};

export { AdminPageSidebar };
