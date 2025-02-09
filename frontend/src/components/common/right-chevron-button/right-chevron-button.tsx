import classNames from 'classnames';

import { MaterialSymbol } from '@/components/common';

import styles from './styles.module.scss';

type Props = Omit<React.ComponentPropsWithoutRef<'button'>, 'children'>;

const RightChevronButton: React.FC<Props> = ({ className, ...props }) => {
  return (
    <button {...props} className={classNames(styles.button, className)}>
      <MaterialSymbol className={styles.symbol}>chevron_right</MaterialSymbol>
    </button>
  );
};

export { RightChevronButton };
