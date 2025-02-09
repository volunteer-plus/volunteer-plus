import classNames from 'classnames';
import { createElement, useMemo } from 'react';
import { Link } from 'react-router-dom';

import styles from './styles.module.scss';

interface Props {
  to?: string;
  rel?: string;
  children: React.ReactNode;
  isActive?: boolean;
}

const ButtonAnchor: React.FC<Props> = ({ to, children, rel, isActive }) => {
  const elementType = useMemo<React.ElementType>(() => {
    return to ? Link : 'div';
  }, [to]);

  return createElement(
    elementType,
    {
      to,
      rel,
      className: classNames(styles.anchor, {
        [styles.active]: isActive,
      }),
    },
    <span>{children}</span>
  );
};

export { ButtonAnchor };
