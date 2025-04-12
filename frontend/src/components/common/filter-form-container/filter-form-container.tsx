import { createElement } from 'react';

import styles from './styles.module.scss';
import classNames from 'classnames';

type Props<E extends React.ElementType> = {
  as?: E;
} & React.ComponentPropsWithoutRef<E>;

const FilterFormContainer = <E extends React.ElementType = 'form'>({
  as,
  className,
  ...props
}: Props<E>): React.ReactNode => {
  return createElement(as ?? 'form', {
    className: classNames(styles.container, className),
    ...props,
  });
};

export { FilterFormContainer };
