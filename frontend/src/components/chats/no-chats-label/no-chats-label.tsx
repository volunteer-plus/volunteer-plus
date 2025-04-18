import classNames from 'classnames';
import { MaterialSymbol } from '@/components/common';

import styles from './styles.module.scss';

const NoChatsLabel: React.FC<React.ComponentPropsWithoutRef<'div'>> = ({
  className,
  ...props
}) => {
  return (
    <div {...props} className={classNames(styles.root, className)}>
      <MaterialSymbol className={styles.icon}>forum</MaterialSymbol>
      <div className={styles.message}>
        Тут будуть ваші чати. Вони зв'являться, щойно ви розпочнете спілкування
        з кимось.
      </div>
    </div>
  );
};

export { NoChatsLabel };
