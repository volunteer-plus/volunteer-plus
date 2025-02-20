import { MenuContainer } from '@/components/common';

import classNames from 'classnames';

const SelectFieldMenuContainer: React.FC<
  React.ComponentPropsWithoutRef<typeof MenuContainer>
> = ({ className, ...props }) => {
  return (
    <MenuContainer
      {...props}
      className={classNames(className)}
      borderColor='var(--color-gray-100)'
    />
  );
};

export { SelectFieldMenuContainer };
