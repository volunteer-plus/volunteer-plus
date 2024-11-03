import React from 'react';
import classNames from 'classnames';

import styles from './styles.module.scss';

type Props = {
  as?: React.ElementType;
} & React.ComponentPropsWithoutRef<'form'>;

const AuthForm: React.FC<Props> = ({ as = 'form', ...props }) => {
  return React.createElement(as, {
    ...props,
    className: classNames(styles.form, props.className),
  });
};

export { AuthForm };
