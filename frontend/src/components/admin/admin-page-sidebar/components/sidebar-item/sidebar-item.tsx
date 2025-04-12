import { Link, useLocation } from 'react-router-dom';

import classNames from 'classnames';

import { SidebarItemConfig } from '../../types';
import styles from './styles.module.scss';
import { useMemo, useState } from 'react';
import { MaterialSymbol } from '@/components/common';

interface Props {
  config: SidebarItemConfig;
}

const SidebarItem: React.FC<Props> = ({ config }) => {
  const [isExpanded, setIsExpanded] = useState(false);

  const location = useLocation();

  const isActive = useMemo(() => {
    if (location.pathname === config.path) {
      return true;
    }

    if (config.subPaths) {
      return config.subPaths.some((subPath) => {
        if (typeof subPath === 'string') {
          return location.pathname === subPath;
        }

        return subPath.test(location.pathname);
      });
    }

    return false;
  }, [config.path, config.subPaths, location.pathname]);

  return (
    <Link
      to={config.path}
      className={classNames(styles.item, {
        [styles.expanded]: isExpanded,
        [styles.active]: isActive,
      })}
      onClick={() => setIsExpanded(!isExpanded)}
    >
      <MaterialSymbol className={styles.icon}>{config.iconName}</MaterialSymbol>
      <p className={styles.label}>{config.label}</p>
    </Link>
  );
};

export { SidebarItem };
