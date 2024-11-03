import classNames from 'classnames';
import { Link as RouterLink } from 'react-router-dom';

import styles from './styles.module.scss';

const Link: React.FC<React.ComponentPropsWithoutRef<typeof RouterLink>> = ({
  className,
  ...props
}) => {
  return (
    <RouterLink {...props} className={classNames(styles.link, className)} />
  );
};

export { Link };
