import classNames from 'classnames';
import React from 'react';
import { animated } from '@react-spring/web';

import styles from './styles.module.scss';

interface Props extends React.ComponentPropsWithoutRef<'div'> {
  borderColor?: string;
}

const MenuContainer = React.forwardRef<HTMLDivElement, Props>(
  ({ className, borderColor = 'var(--color-gray-300)', ...props }, ref) => {
    return (
      <animated.div
        {...props}
        className={classNames(styles.container, className)}
        ref={ref}
        style={
          {
            '--menu-container-border-color': borderColor,
            ...props.style,
          } as React.CSSProperties
        }
      />
    );
  }
);

export { MenuContainer };
