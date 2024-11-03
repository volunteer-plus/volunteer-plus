import React from 'react';
import classNames from 'classnames';

import styles from './styles.module.scss';

const AuthFormBody: React.FC<React.ComponentPropsWithoutRef<'div'>> = (
  props
) => {
  return (
    <div {...props} className={classNames(styles.body, props.className)} />
  );
};

export { AuthFormBody };
