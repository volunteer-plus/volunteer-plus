import classNames from 'classnames';

import { Avatar } from '@/components/common';

import styles from './styles.module.scss';

type Props = Omit<React.ComponentPropsWithoutRef<'div'>, 'children'> & {
  name: React.ReactNode;
  role?: React.ReactNode;
  avatarImageSrc?: string;
};

const UserItem: React.FC<Props> = ({
  className,
  name,
  role,
  avatarImageSrc,
  ...props
}) => {
  return (
    <div {...props} className={classNames(styles.root, className)}>
      <Avatar size='32px' imageSrc={avatarImageSrc} />
      <div>
        <div className={styles.name}>{name}</div>
        {role && <div className={styles.role}>{role}</div>}
      </div>
    </div>
  );
};

export { UserItem };
