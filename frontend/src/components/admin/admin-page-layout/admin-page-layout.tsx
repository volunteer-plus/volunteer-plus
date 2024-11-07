import classNames from 'classnames';

import { AdminPageSidebarContext } from '@/contexts';

import styles from './styles.module.scss';
import { useCallback, useState } from 'react';

const AdminPageLayout: React.FC<React.ComponentPropsWithoutRef<'div'>> = ({
  className,
  ...props
}) => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);

  const openSidebar = useCallback(() => {
    setIsSidebarOpen(true);
  }, []);

  const closeSidebar = useCallback(() => {
    setIsSidebarOpen(false);
  }, []);

  const toggleSidebar = useCallback(() => {
    setIsSidebarOpen((prev) => !prev);
  }, []);

  return (
    <AdminPageSidebarContext.Provider
      value={{
        close: closeSidebar,
        open: openSidebar,
        toggle: toggleSidebar,
        isOpen: isSidebarOpen,
      }}
    >
      <div {...props} className={classNames(styles.layout, className)} />
    </AdminPageSidebarContext.Provider>
  );
};

export { AdminPageLayout };
