import React, { useCallback, useState } from 'react';
import classNames from 'classnames';

import { FieldBodyVariant } from '@/types/common';
import { FieldDescription } from '@/components/common';

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
  units?: React.ReactNode;
};

function getClassNameForVariant(variant: FieldBodyVariant) {
  return styles[`${variant}Variant`];
}

const FieldBody = React.forwardRef<HTMLDivElement, Props>(
  (
    {
      children,
      variant = 'default',
      isDisabled,
      description,
      leftIcon,
      rightIcon,
      className,
      units,
      ...props
    },
    ref
  ) => {
    const [isFocused, setIsFocused] = useState(false);

    const onFocus = useCallback(() => {
      setIsFocused(true);
    }, [setIsFocused]);

    const onBlur = useCallback(() => {
      setIsFocused(false);
    }, [setIsFocused]);

    return (
      <div
        {...props}
        className={classNames(
          styles.root,
          className,
          getClassNameForVariant(variant),
          {
            [styles.focused]: isFocused,
            [styles.disabled]: isDisabled,
            [styles.withLeftIcon]: !!leftIcon,
            [styles.withRightIcon]: !!rightIcon,
            [styles.withUnits]: !!units,
          }
        )}
        ref={ref}
      >
        <div className={styles.body}>
          {leftIcon && <div className={styles.leftIcon}>{leftIcon}</div>}
          {units && <div className={styles.units}>{units}</div>}
          {children({
            onFocus,
            onBlur,
            className: styles.children,
          })}
          {rightIcon && <div className={styles.rightIcon}>{rightIcon}</div>}
        </div>
        {description && (
          <FieldDescription variant={variant} className={styles.description}>
            {description}
          </FieldDescription>
        )}
      </div>
    );
  }
);

export { FieldBody };
