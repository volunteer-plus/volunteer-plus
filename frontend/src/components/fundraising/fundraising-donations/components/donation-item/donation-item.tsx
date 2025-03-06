import classNames from 'classnames';

import styles from './styles.module.scss';
import { formatDateAndTime, formatMoneyAmount } from '@/helpers/common';

type Props = Omit<React.ComponentPropsWithoutRef<'div'>, 'children'> & {
  amount: number;
  email: string;
  date: Date;
};

const DonationItem: React.FC<Props> = ({
  className,
  amount,
  email,
  date,
  ...props
}) => {
  return (
    <div {...props} className={classNames(styles.root, className)}>
      <div className={styles.amountAndEmail}>
        <div className={styles.amount}>₴{formatMoneyAmount(amount)}</div>
        <div className={styles.email}>від {email}</div>
      </div>
      <div className={styles.date}>{formatDateAndTime(date)}</div>
    </div>
  );
};

export { DonationItem };
