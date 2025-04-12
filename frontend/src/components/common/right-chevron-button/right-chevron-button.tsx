import { createElement } from 'react';

import { MaterialSymbol } from '@/components/common';

import styles from './styles.module.scss';
import classNames from 'classnames';

type Props<E extends React.ElementType> = {
  as?: E;
  size?: string;
} & Omit<React.ComponentPropsWithoutRef<E>, 'children'>;

const RightChevronButton = <E extends React.ElementType = 'button'>({
  as,
  className,
  size = '32px',
  ...props
}: Props<E>): React.ReactNode => {
  return createElement(as ?? 'button', {
    ...props,
    style: {
      ...props.style,
      '--right-chevron-button-size': size,
    },
    className: classNames(styles.button, className),
    children: (
      <MaterialSymbol className={styles.symbol}>chevron_right</MaterialSymbol>
    ),
  });
};

export { RightChevronButton };
