import { RightChevronButton } from '@/components/common';
import { Link } from 'react-router-dom';
import classNames from 'classnames';

import styles from './styles.module.scss';

interface Props
  extends Omit<React.ComponentPropsWithoutRef<typeof Link>, 'to'> {
  brigadeId: number;
  brigadeName: string;
}

const BrigadeListItem: React.FC<Props> = ({
  className,
  brigadeId,
  brigadeName,
  ...props
}) => {
  return (
    <Link
      {...props}
      className={classNames(styles.item, className)}
      to={`/brigades/${brigadeId}`}
    >
      <span className={styles.brigadeName}>{brigadeName}</span>
      <RightChevronButton />
    </Link>
  );
};

export { BrigadeListItem };
