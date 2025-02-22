import classNames from 'classnames';
import { MaterialSymbol } from '@/components/common';

import styles from './styles.module.scss';

const ChatNotSelectedPlaceholder: React.FC<
  React.ComponentPropsWithoutRef<'div'>
> = ({ className, ...props }) => {
  return (
    <div {...props} className={classNames(styles.placeholder, className)}>
      <div className={styles.placeholderContent}>
        <MaterialSymbol className={styles.icon}>forum</MaterialSymbol>
        <div className={styles.message}>
          Оберіть чат, щоб розпочати спілкування
        </div>
      </div>
    </div>
  );
};

export { ChatNotSelectedPlaceholder };
