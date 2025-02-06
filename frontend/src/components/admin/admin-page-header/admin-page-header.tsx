import Logo from '@/assets/logo.svg?react';

import styles from './styles.module.scss';
import { useAdminPageSidebarContext } from '@/hooks/admin';
import { SessionUserBlock } from '@/components/common/session-user-block';

const AdminPageHeader: React.FC = () => {
  const { toggle: toggleSidebar } = useAdminPageSidebarContext();

  return (
    <header className={styles.header}>
      <div className={styles.leftPart}>
        <div className={styles.burgerWrapper}>
          <button
            type='button'
            className={styles.burgerButton}
            onClick={toggleSidebar}
          >
            <span className='material-symbols-outlined'>menu</span>
          </button>
        </div>
        <Logo className={styles.logo} />
      </div>
      <div className={styles.rightPart}>
        <SessionUserBlock />
      </div>
    </header>
  );
};

export { AdminPageHeader };
