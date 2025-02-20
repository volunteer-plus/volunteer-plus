import classNames from 'classnames';

import styles from './styles.module.scss';
import { MaterialSymbol } from '@/components/common/material-symbol';

interface Props extends React.ComponentPropsWithRef<'div'> {
  onRemoveClick?: React.MouseEventHandler<HTMLButtonElement>;
}

const SelectedOption: React.FC<Props> = ({
  children,
  className,
  onRemoveClick,
  ...props
}) => {
  return (
    <div {...props} className={classNames(styles.option, className)}>
      <div className={styles.optionLabel}>{children}</div>
      <button
        className={styles.removeButton}
        type='button'
        onClick={onRemoveClick}
      >
        <MaterialSymbol className={styles.removeIcon}>close</MaterialSymbol>
      </button>
    </div>
  );
};

export { SelectedOption };
