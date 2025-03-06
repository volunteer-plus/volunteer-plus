import classNames from 'classnames';

import { GrayContainer } from '@/components/common';

import styles from './styles.module.scss';
import { DonationItem } from './components';

type Props = Omit<
  React.ComponentPropsWithoutRef<typeof GrayContainer>,
  'children'
>;

const FundraisingDonations: React.FC<Props> = ({ className, ...props }) => {
  return (
    <GrayContainer {...props} className={classNames(styles.root, className)}>
      <DonationItem amount={100} email='test@a.com' date={new Date()} />
      <DonationItem amount={100000} email='test@a.com' date={new Date()} />
      <DonationItem amount={10} email='test@a.com' date={new Date()} />
      <DonationItem amount={30} email='test@a.com' date={new Date()} />
      <DonationItem amount={100} email='test@a.com' date={new Date()} />
    </GrayContainer>
  );
};

export { FundraisingDonations };
