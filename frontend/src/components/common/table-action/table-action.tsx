import classNames from 'classnames';
import styles from './styles.module.scss';
import { MaterialSymbol } from '@/components/common';

interface Props extends Omit<React.ComponentProps<'button'>, 'children'> {
  children: React.ComponentProps<typeof MaterialSymbol>['children'];
}

const TableAction: React.FC<Props> = ({ className, children, ...props }) => {
  return (
    <button {...props} className={classNames(className, styles.action)}>
      <MaterialSymbol className={styles.icon}>{children}</MaterialSymbol>
    </button>
  );
};

export { TableAction };
