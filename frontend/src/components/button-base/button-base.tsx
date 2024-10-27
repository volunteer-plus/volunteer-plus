import {
  ButtonBaseColorSchema,
  ButtonBaseStyleProps,
  ButtonBaseVariant,
} from '@/types';
import classNames from 'classnames';
import React from 'react';
import styles from './styles.module.scss';

type Props<T extends React.ElementType = React.ElementType> =
  ButtonBaseStyleProps & {
    elementType: T;
    elementProps?: React.ComponentPropsWithoutRef<T>;
  };

function getClassNameForColorSchema(colorSchema: ButtonBaseColorSchema) {
  return styles[`${colorSchema}ColorSchema`];
}

function getClassNameForVariant(variant: ButtonBaseVariant) {
  return styles[`${variant}Variant`];
}

const ButtonBase = React.forwardRef<HTMLElement, Props>(
  (
    {
      elementType,
      elementProps = {},
      variant = 'filled',
      colorSchema = 'olive',
    },
    ref
  ) => {
    return React.createElement(elementType, {
      ...elementProps,
      ref,
      className: classNames(
        elementProps.className,
        getClassNameForColorSchema(colorSchema),
        getClassNameForVariant(variant)
      ),
    });
  }
);

export { ButtonBase };
