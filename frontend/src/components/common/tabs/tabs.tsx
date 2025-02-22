import { TabConfig } from './types';

import styles from './styles.module.scss';
import classNames from 'classnames';

interface Props extends React.ComponentPropsWithoutRef<'div'> {
  tabsConfigs: TabConfig[];
  activeTabKey?: string;
  onTabChange?: (tabKey: string) => void;
}

const Tabs: React.FC<Props> = ({
  tabsConfigs,
  onTabChange,
  className,
  activeTabKey,
  ...props
}) => {
  return (
    <div {...props} className={classNames(styles.container, className)}>
      {tabsConfigs.map((tabConfig) => (
        <button
          type='button'
          onClick={() => {
            onTabChange?.(tabConfig.key);
          }}
          className={classNames(styles.tab, {
            [styles.active]: tabConfig.key === activeTabKey,
          })}
          key={tabConfig.key}
        >
          {tabConfig.label}
        </button>
      ))}
    </div>
  );
};

export { Tabs };
