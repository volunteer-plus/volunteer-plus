import { FieldBody, FieldLabel, makeFormikField } from '@/components/common';
import classNames from 'classnames';

import styles from './styles.module.scss';
import { FieldBodyVariant } from '@/types/common';
import { useCallback, useEffect, useRef } from 'react';

type Props = {
  label?: React.ReactNode;
  isRequired?: boolean;
  variant?: FieldBodyVariant;
  description?: React.ReactNode;
  leftIcon?: React.ReactNode;
  rightIcon?: React.ReactNode;
  type?: 'text' | 'password';
  autoSize?: boolean;
} & React.ComponentPropsWithoutRef<'textarea'>;

const TextareaField: React.FC<Props> = ({
  label,
  variant,
  isRequired = false,
  disabled,
  description,
  onFocus: onFocusProp,
  onBlur: onBlurProp,
  leftIcon,
  rightIcon,
  className,
  autoSize,
  value,
  ...props
}) => {
  const textareaRef = useRef<HTMLTextAreaElement>(null);

  const updateHeight = useCallback(() => {
    const textarea = textareaRef.current;

    if (!textarea) {
      return;
    }

    textarea.style.height = 'auto';
    textarea.style.height = `${textarea.scrollHeight}px`;
  }, []);

  useEffect(() => {
    if (!autoSize) {
      return;
    }

    updateHeight();
  }, [autoSize, updateHeight]);

  useEffect(() => {
    if (!autoSize) {
      return;
    }

    updateHeight();
  }, [value, updateHeight, autoSize]);

  return (
    <div className={className}>
      {label && (
        <FieldLabel isRequired={isRequired} className={styles.label}>
          {label}
        </FieldLabel>
      )}
      <FieldBody
        variant={variant}
        isDisabled={disabled}
        description={description}
        leftIcon={leftIcon}
        rightIcon={rightIcon}
      >
        {({ onFocus, onBlur, className }) => {
          return (
            <textarea
              {...props}
              value={value}
              onFocus={(event) => {
                onFocus();
                onFocusProp?.(event);
              }}
              onBlur={(event) => {
                onBlur();
                onBlurProp?.(event);
              }}
              disabled={disabled}
              className={classNames(className, styles.input, {
                [styles.autoSize]: autoSize,
              })}
              ref={textareaRef}
            />
          );
        }}
      </FieldBody>
    </div>
  );
};

const FormikTextareaField = makeFormikField(TextareaField);

export { TextareaField, FormikTextareaField };
