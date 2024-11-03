import { useCallback, useState } from 'react';
import classNames from 'classnames';

import { FieldBodyVariant } from '@/types/common';
import styles from './styles.module.scss';

type RenderChildrenProps = {
  onFocus: () => void;
  onBlur: () => void;
  className: string;
};

type RenderChildren = (props: RenderChildrenProps) => React.ReactNode;

type Props = Omit<React.ComponentPropsWithoutRef<'div'>, 'children'> & {
  children: RenderChildren;
  description?: React.ReactNode;
  variant?: FieldBodyVariant;
  isDisabled?: boolean;
  rightIcon?: React.ReactNode;
  leftIcon?: React.ReactNode;
};

function getClassNameForVariant(variant: FieldBodyVariant) {
  return styles[`${variant}Variant`];
}

const FieldBody: React.FC<Props> = ({
  children,
  variant = 'default',
  isDisabled,
  description,
  leftIcon,
  rightIcon,
}) => {
  const [isFocused, setIsFocused] = useState(false);

  const onFocus = useCallback(() => {
    setIsFocused(true);
  }, [setIsFocused]);

  const onBlur = useCallback(() => {
    setIsFocused(false);
  }, [setIsFocused]);

  return (
    <div
      className={classNames(styles.root, getClassNameForVariant(variant), {
        [styles.focused]: isFocused,
        [styles.disabled]: isDisabled,
        [styles.withLeftIcon]: !!leftIcon,
        [styles.withRightIcon]: !!rightIcon,
      })}
    >
      <div className={styles.body}>
        {leftIcon && <div className={styles.leftIcon}>{leftIcon}</div>}
        {children({
          onFocus,
          onBlur,
          className: styles.children,
        })}
        {rightIcon && <div className={styles.rightIcon}>{rightIcon}</div>}
      </div>
      {description && <div className={styles.description}>{description}</div>}
    </div>
  );
};

export { FieldBody };
