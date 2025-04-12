import { MaterialSymbol } from '@/components/common';
import classNames from 'classnames';

import styles from './styles.module.scss';

type Props = Omit<React.ComponentPropsWithoutRef<'input'>, 'type'>;

const Checkbox: React.FC<Props> = ({ className, ...props }) => {
  return (
    <label className={classNames(styles.label, className)}>
      <input {...props} type='checkbox' className={styles.input} />
      <div className={styles.checkbox}>
        <MaterialSymbol className={styles.checkmark}>check</MaterialSymbol>
      </div>
    </label>
  );
};

export { Checkbox };
