import { FieldBody, FieldLabel } from '@/components/common';
import classNames from 'classnames';

import styles from './styles.module.scss';
import { FieldBodyVariant } from '@/types/common';

type Props = {
  label?: React.ReactNode;
  isRequired?: boolean;
  variant?: FieldBodyVariant;
  description?: React.ReactNode;
  leftIcon?: React.ReactNode;
  rightIcon?: React.ReactNode;
  type?: 'text' | 'password';
} & Omit<React.ComponentPropsWithoutRef<'input'>, 'type'>;

const TextInputField: React.FC<Props> = ({
  label,
  variant,
  isRequired = false,
  disabled,
  description,
  type = 'text',
  onFocus: onFocusProp,
  onBlur: onBlurProp,
  leftIcon,
  rightIcon,
  ...props
}) => {
  return (
    <div>
      {label && <FieldLabel isRequired={isRequired}>{label}</FieldLabel>}
      <FieldBody
        variant={variant}
        isDisabled={disabled}
        description={description}
        leftIcon={leftIcon}
        rightIcon={rightIcon}
      >
        {({ onFocus, onBlur, className }) => {
          return (
            <input
              {...props}
              type={type}
              onFocus={(event) => {
                onFocus();
                onFocusProp?.(event);
              }}
              onBlur={(event) => {
                onBlur();
                onBlurProp?.(event);
              }}
              disabled={disabled}
              className={classNames(className, styles.input)}
            />
          );
        }}
      </FieldBody>
    </div>
  );
};

export { TextInputField };
