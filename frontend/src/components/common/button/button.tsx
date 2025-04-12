import React from 'react';
import { ButtonBase } from '@/components/common';
import { ButtonBaseStyleProps } from '@/types/common';

type Props = React.ComponentPropsWithoutRef<'button'> & ButtonBaseStyleProps;

const Button = React.forwardRef<HTMLButtonElement, Props>(
  ({ colorSchema, variant, disabled, leftIcon, rightIcon, ...props }, ref) => {
    return (
      <ButtonBase
        elementType='button'
        elementProps={{
          type: 'button',
          disabled,
          ...props,
        }}
        colorSchema={colorSchema}
        variant={disabled ? 'disabled' : variant}
        ref={ref}
        leftIcon={leftIcon}
        rightIcon={rightIcon}
      />
    );
  }
);

export { Button };
