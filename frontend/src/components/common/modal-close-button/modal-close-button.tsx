import classNames from 'classnames';

import { MaterialSymbol } from '@/components/common';

import styles from './styles.module.scss';

const ModalCloseButton: React.FC<
  Omit<React.ComponentPropsWithoutRef<'button'>, 'type' | 'children'>
> = ({ className, ...props }) => {
  return (
    <button
      {...props}
      className={classNames(styles.button, className)}
      type='button'
    >
      <MaterialSymbol>close</MaterialSymbol>
    </button>
  );
};

export { ModalCloseButton };
