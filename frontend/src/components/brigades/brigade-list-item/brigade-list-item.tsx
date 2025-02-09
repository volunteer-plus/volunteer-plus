import { RightChevronButton } from '@/components/common';

import styles from './styles.module.scss';
import { Link } from 'react-router-dom';
import classNames from 'classnames';

const BrigadeListItem: React.FC<React.HTMLAttributes<HTMLAnchorElement>> = ({
  className,
  ...props
}) => {
  return (
    <Link
      {...props}
      className={classNames(styles.item, className)}
      to={`/brigades/1`}
    >
      <span className={styles.brigadeName}>Третя ОШБ</span>
      <RightChevronButton />
    </Link>
  );
};

export { BrigadeListItem };
