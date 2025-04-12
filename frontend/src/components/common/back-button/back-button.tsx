import { createElement } from 'react';

import styles from './styles.module.scss';
import classNames from 'classnames';
import { MaterialSymbol } from '../material-symbol';

type Props<E extends React.ElementType> = {
  as?: E;
} & React.ComponentPropsWithoutRef<E>;

const BackButton = <E extends React.ElementType = 'button'>({
  as,
  className,
  children,
  ...props
}: Props<E>): React.ReactNode => {
  return createElement(as ?? 'button', {
    className: classNames(styles.button, className),
    children: (
      <>
        <MaterialSymbol className={styles.icon}>
          chevron_backward
        </MaterialSymbol>
        {children}
      </>
    ),
    ...props,
  });
};

export { BackButton };
