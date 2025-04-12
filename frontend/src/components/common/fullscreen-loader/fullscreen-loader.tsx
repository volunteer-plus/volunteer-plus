import React from 'react';
import classNames from 'classnames';

import { BarsLoader } from '@/components/common';
import styles from './styles.module.scss';

const FullscreenLoader: React.FC<React.ComponentPropsWithoutRef<'div'>> = (
  props
) => {
  return (
    <div {...props} className={classNames(styles.wrapper, props.className)}>
      <BarsLoader size='50px' />
    </div>
  );
};

export { FullscreenLoader };
