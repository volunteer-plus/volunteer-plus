import { Link, useLocation } from 'react-router-dom';

import classNames from 'classnames';

import { SidebarItemConfig } from '../../types';
import styles from './styles.module.scss';
import { useMemo, useState } from 'react';

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
      return config.subPaths.some((subPath) => location.pathname === subPath);
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
      <span className={classNames('material-symbols-outlined', styles.icon)}>
        {config.iconName}
      </span>
      <p className={styles.label}>{config.label}</p>
    </Link>
  );
};

export { SidebarItem };
