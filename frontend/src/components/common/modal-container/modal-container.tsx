import classNames from 'classnames';
import React from 'react';
import { animated } from '@react-spring/web';

import styles from './styles.module.scss';

interface Props extends React.ComponentPropsWithoutRef<'div'> {
  width?: string;
}

const ModalContainer = React.forwardRef<HTMLDivElement, Props>(
  ({ className, width = '450px', style, ...props }, ref) => {
    return (
      <animated.div
        {...props}
        className={classNames(styles.container, className)}
        ref={ref}
        style={
          { ...style, '--modal-container-width': width } as React.CSSProperties
        }
      />
    );
  }
);

export { ModalContainer };
