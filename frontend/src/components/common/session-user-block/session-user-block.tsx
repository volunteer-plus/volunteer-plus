import { useRef, useState } from 'react';
import classNames from 'classnames';
import {
  Avatar,
  ExpandIcon,
  Menu,
  MenuContainer,
  MenuItem,
  MenuItemIconMaterial,
} from '@/components/common';

import styles from './styles.module.scss';
import { useClickOutside } from '@/hooks/common';
import { useLogout } from '@/hooks/auth';
import { useAppSelector } from '@/hooks/store';
import { getFullName } from '@/helpers/user';

type Props = React.ComponentPropsWithoutRef<'button'>;

const SessionUserBlock: React.FC<Props> = ({ className, ...props }) => {
  const logout = useLogout();
  const { user } = useAppSelector((state) => state.user);

  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const blockRef = useRef<HTMLButtonElement>(null);

  const { handlers: clickOutsideHandlers } = useClickOutside({
    callback: () => setIsMenuOpen(false),
    isEnabled: isMenuOpen,
  });

  return (
    <>
      <button
        {...props}
        {...clickOutsideHandlers}
        className={classNames(styles.userBlock, className)}
        type='button'
        onClick={() => setIsMenuOpen(!isMenuOpen)}
        ref={blockRef}
      >
        <ExpandIcon isExpanded={isMenuOpen} />
        {user && <div>{getFullName(user)}</div>}

        <Avatar size='30px' />
      </button>
      <Menu
        isOpen={isMenuOpen}
        targetRef={blockRef}
        side='bottom'
        alignment='stretch'
      >
        <MenuContainer {...clickOutsideHandlers}>
          <MenuItem
            leftIcon={<MenuItemIconMaterial>logout</MenuItemIconMaterial>}
            onClick={() => logout()}
          >
            Вийти
          </MenuItem>
        </MenuContainer>
      </Menu>
    </>
  );
};

export { SessionUserBlock };
