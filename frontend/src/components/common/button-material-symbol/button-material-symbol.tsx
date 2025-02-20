import classNames from 'classnames';
import { MaterialSymbol } from '@/components/common';

import styles from './styles.module.scss';

const ButtonMaterialSymbol: React.FC<
  React.ComponentPropsWithoutRef<typeof MaterialSymbol>
> = ({ className, ...props }) => {
  return (
    <MaterialSymbol
      {...props}
      className={classNames(styles.symbol, className)}
    />
  );
};

export { ButtonMaterialSymbol };
