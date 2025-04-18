import { Link } from 'react-router-dom';
import classNames from 'classnames';
import { animated } from '@react-spring/web';

import { Avatar, GrowTransition } from '@/components/common';

import styles from './styles.module.scss';

interface Props
  extends Omit<React.ComponentPropsWithoutRef<typeof Link>, 'to'> {
  userFullName: React.ReactNode;
  userRole?: React.ReactNode;
  userAvatarImageSrc?: string;
  isUserOnline?: boolean;
  newMessagesCount?: number;
  isActive?: boolean;
  path: string;
}

const ChatsListItem: React.FC<Props> = ({
  userFullName,
  userRole,
  newMessagesCount,
  userAvatarImageSrc,
  isActive,
  className,
  path,
  isUserOnline = false,
  ...props
}) => {
  return (
    <Link
      {...props}
      className={classNames(
        styles.item,
        { [styles.active]: isActive },
        className
      )}
      to={path}
    >
      <div className={styles.avatarWrapper}>
        <Avatar size='36px' imageSrc={userAvatarImageSrc} />
        <GrowTransition on={isUserOnline}>
          <animated.div className={styles.onlineIndicator} />
        </GrowTransition>
      </div>
      <div className={styles.userInfo}>
        <div className={styles.name}>{userFullName}</div>
        {userRole && <div className={styles.role}>{userRole}</div>}
      </div>
      {Boolean(newMessagesCount) && (
        <div className={styles.newMessagesCount}>{newMessagesCount}</div>
      )}
    </Link>
  );
};

export { ChatsListItem };
