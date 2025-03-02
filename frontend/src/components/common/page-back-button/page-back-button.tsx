import classNames from 'classnames';

import { BackButton } from '@/components/common/back-button';

import styles from './styles.module.scss';

type Props = React.ComponentPropsWithoutRef<typeof BackButton>;

const PageBackButton: React.FC<Props> = ({ className, ...props }) => {
  return (
    <BackButton {...props} className={classNames(styles.button, className)} />
  );
};

export { PageBackButton };
