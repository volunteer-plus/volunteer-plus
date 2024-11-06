import React from 'react';
import classNames from 'classnames';

import styles from './styles.module.scss';
import { Bars } from 'react-loader-spinner';

const FullscreenLoader: React.FC<React.ComponentPropsWithoutRef<'div'>> = (
  props
) => {
  return (
    <div {...props} className={classNames(styles.wrapper, props.className)}>
      <Bars
        height='50'
        width='50'
        color='var(--color-olive-300)'
        ariaLabel='bars-loading'
        visible={true}
      />
    </div>
  );
};

export { FullscreenLoader };
