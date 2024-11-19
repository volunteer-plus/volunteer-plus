import classNames from 'classnames';
import React from 'react';
import { animated } from '@react-spring/web';

import styles from './styles.module.scss';

const ModalContainer = React.forwardRef<
  HTMLDivElement,
  React.ComponentPropsWithoutRef<'div'>
>(({ className, ...props }, ref) => {
  return (
    <animated.div
      {...props}
      className={classNames(styles.container, className)}
      ref={ref}
    />
  );
});

export { ModalContainer };
