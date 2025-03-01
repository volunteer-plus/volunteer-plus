import React, {
  useCallback,
  useEffect,
  useMemo,
  useRef,
  useState,
} from 'react';
import { FieldBodyVariant, SelectOption } from '@/types/common';
import {
  ExpandIcon,
  FieldBody,
  FieldLabel,
  makeFormikField,
  Menu,
  SelectFieldMenuContainer,
  SelectFieldMenuItem,
  SelectFieldNoOptionsMessage,
} from '@/components/common';
import { useClickOutside } from '@/hooks/common';

import styles from './styles.module.scss';
import classNames from 'classnames';

type Props = {
  label?: React.ReactNode;
  isRequired?: boolean;
  variant?: FieldBodyVariant;
  description?: React.ReactNode;
  options: SelectOption[];
  value: unknown;
  onChange?: (nextValue: unknown) => void;
  onBlur?: () => void;
  placeholder?: string;
  isClearable?: boolean;
};

const SelectField: React.FC<Props> = ({
  options,
  value,
  onChange,
  label,
  isRequired,
  variant,
  description,
  onBlur,
  placeholder,
  isClearable = false,
}) => {
  const fieldRef = useRef<HTMLDivElement>(null);
  const inputRef = useRef<HTMLInputElement>(null);

  const [isFocused, setIsFocused] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');

  const selectedOption = useMemo(() => {
    return options.find((option) => option.value === value);
  }, [options, value]);

  const foundOptions = useMemo(() => {
    if (!searchQuery) {
      return options;
    }

    return options.filter((option) =>
      option.label.toLowerCase().includes(searchQuery.toLowerCase())
    );
  }, [options, searchQuery]);

  const { handlers: clickOutsideSelectHandlers } = useClickOutside({
    callback: () => blur(),
    isEnabled: isFocused,
  });

  useEffect(() => {
    setSearchQuery('');
  }, [isFocused]);

  const onInputChange = useCallback<React.ChangeEventHandler<HTMLInputElement>>(
    (event) => {
      setSearchQuery(event.target.value);
    },
    []
  );

  const onInputFocus = useCallback(() => {
    setIsFocused(true);
  }, []);

  const blur = useCallback(() => {
    onBlur?.();
    setIsFocused(false);
  }, [onBlur]);

  const focus = useCallback(() => {
    inputRef.current?.focus();
  }, []);

  const setValueAndBlur = useCallback(
    async (nextValue: unknown) => {
      await onChange?.(nextValue);
      blur();
    },
    [onChange, blur]
  );

  return (
    <div>
      {label && (
        <FieldLabel isRequired={isRequired} className={styles.label}>
          {label}
        </FieldLabel>
      )}
      <FieldBody
        ref={fieldRef}
        rightIcon={
          <ExpandIcon
            isExpanded={isFocused}
            className={styles.chevron}
            onClick={() => (isFocused ? blur() : focus())}
          />
        }
        variant={variant}
        description={description}
        onFocus={onInputFocus}
        {...clickOutsideSelectHandlers}
      >
        {({ className, ...props }) => {
          return (
            <>
              <input
                {...props}
                className={classNames(className, styles.input)}
                ref={inputRef}
                value={isFocused ? searchQuery : selectedOption?.label || ''}
                onChange={onInputChange}
                placeholder={placeholder}
              />
              <Menu isOpen={isFocused} targetRef={fieldRef} alignment='stretch'>
                <SelectFieldMenuContainer>
                  {foundOptions.length === 0 && <SelectFieldNoOptionsMessage />}
                  {foundOptions.map((option, index) => {
                    const isSelected = value === option.value;

                    return (
                      <SelectFieldMenuItem
                        key={index}
                        onClick={() => {
                          if (isSelected) {
                            if (!isClearable) {
                              return;
                            }

                            setValueAndBlur(null);
                          } else {
                            setValueAndBlur(option.value);
                          }
                        }}
                        isSelected={isSelected}
                      >
                        {option.label}
                      </SelectFieldMenuItem>
                    );
                  })}
                </SelectFieldMenuContainer>
              </Menu>
            </>
          );
        }}
      </FieldBody>
    </div>
  );
};

const FormikSelectField = makeFormikField(SelectField, {
  nativeEvents: false,
});

export { SelectField, FormikSelectField };
