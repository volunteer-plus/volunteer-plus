import classNames from 'classnames';

import styles from './styles.module.scss';
import { formatDateAndTime } from '@/helpers/common';

interface Props extends Omit<React.ComponentPropsWithRef<'div'>, 'children'> {
  children: Date;
}

const MessageSentAt: React.FC<Props> = ({ children, className }) => {
  return (
    <div className={classNames(styles.sentAt, className)}>
      {formatDateAndTime(children)}
    </div>
  );
};

export { MessageSentAt };
