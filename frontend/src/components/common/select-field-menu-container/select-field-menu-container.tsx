import classNames from 'classnames';

import { MenuContainer } from '@/components/common';

import styles from './styles.module.scss';

const SelectFieldMenuContainer: React.FC<
  React.ComponentPropsWithoutRef<typeof MenuContainer>
> = ({ className, ...props }) => {
  return (
    <MenuContainer
      {...props}
      className={classNames(className, styles.container)}
      borderColor='var(--color-gray-100)'
    />
  );
};

export { SelectFieldMenuContainer };
