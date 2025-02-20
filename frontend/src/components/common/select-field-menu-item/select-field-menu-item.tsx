import classNames from 'classnames';

import styles from './styles.module.scss';
import { MaterialSymbol } from '../material-symbol';

interface Props extends React.ComponentPropsWithoutRef<'div'> {
  isSelected?: boolean;
}

const SelectFieldMenuItem: React.FC<Props> = ({
  className,
  isSelected,
  children,
  ...props
}) => {
  return (
    <div
      {...props}
      className={classNames(className, styles.item, {
        [styles.selected]: isSelected,
      })}
    >
      <div className={styles.label}>{children}</div>
      {isSelected && (
        <MaterialSymbol className={styles.checkIcon}>
          check_small
        </MaterialSymbol>
      )}
    </div>
  );
};

export { SelectFieldMenuItem };
