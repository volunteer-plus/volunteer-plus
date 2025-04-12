import classNames from 'classnames';

import styles from './styles.module.scss';
import { RightChevronButton } from '@/components/common';
import { Link } from 'react-router-dom';

type Props = Omit<React.ComponentPropsWithoutRef<'div'>, 'children'> & {
  requestId: number;
  title: React.ReactNode;
  brigadeName: React.ReactNode;
};

const FundraisingRequest: React.FC<Props> = ({
  className,
  requestId,
  title,
  brigadeName,
  ...props
}) => {
  return (
    <div {...props} className={classNames(styles.root, className)}>
      <div>№{requestId}</div>
      <div>{title}</div>
      <div>{brigadeName}</div>
      <RightChevronButton
        size='20px'
        as={Link}
        to={`/volunteer/request/${requestId}`}
      />
    </div>
  );
};

export { FundraisingRequest };
