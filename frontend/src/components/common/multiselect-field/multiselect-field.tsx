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

import { SelectedOption } from './components';
import styles from './styles.module.scss';

type Props = {
  label?: React.ReactNode;
  isRequired?: boolean;
  variant?: FieldBodyVariant;
  description?: React.ReactNode;
  options: SelectOption[];
  value: unknown[];
  onChange?: (nextValue: unknown[]) => void;
  onBlur?: () => void;
  placeholder?: string;
};

const MultiselectField: React.FC<Props> = ({
  options,
  value,
  onChange,
  label,
  isRequired,
  variant,
  description,
  onBlur,
  placeholder,
}) => {
  const fieldRef = useRef<HTMLDivElement>(null);
  const inputRef = useRef<HTMLInputElement>(null);

  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');

  const selectedOptions = useMemo(() => {
    return options.filter((option) => value.includes(option.value));
  }, [options, value]);

  const foundOptions = useMemo(() => {
    if (!searchQuery) {
      return options;
    }

    return options.filter((option) =>
      option.label.toLowerCase().includes(searchQuery.toLowerCase())
    );
  }, [options, searchQuery]);

  const isSelectionEmpty = selectedOptions.length === 0;

  const { handlers: clickOutsideSelectHandlers } = useClickOutside({
    callback: () => blur(),
    isEnabled: isMenuOpen,
  });

  useEffect(() => {
    setSearchQuery('');
  }, [isMenuOpen]);

  const onInputChange = useCallback<React.ChangeEventHandler<HTMLInputElement>>(
    (event) => {
      setSearchQuery(event.target.value);
    },
    []
  );

  const onInputFocus = useCallback(() => {
    setIsMenuOpen(true);
  }, []);

  const blur = useCallback(() => {
    onBlur?.();
    setIsMenuOpen(false);
  }, [onBlur]);

  const focus = useCallback(() => {
    inputRef.current?.focus();
  }, []);

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
            isExpanded={isMenuOpen}
            className={styles.chevron}
            onClick={() => (isMenuOpen ? blur() : focus())}
          />
        }
        variant={variant}
        description={description}
        onFocus={onInputFocus}
      >
        {({ onFocus, onBlur }) => {
          return (
            <div className={styles.fakeInput} {...clickOutsideSelectHandlers}>
              {selectedOptions.map((option, index) => {
                return (
                  <SelectedOption
                    key={index}
                    onRemoveClick={() => {
                      onChange?.(
                        value.filter((value) => value !== option.value)
                      );
                    }}
                  >
                    {option.label}
                  </SelectedOption>
                );
              })}
              <input
                type='text'
                className={styles.input}
                value={searchQuery}
                onChange={onInputChange}
                ref={inputRef}
                placeholder={isSelectionEmpty ? placeholder : ''}
                onFocus={onFocus}
                onBlur={onBlur}
              />
              <Menu
                isOpen={isMenuOpen}
                targetRef={fieldRef}
                alignment='stretch'
              >
                <SelectFieldMenuContainer>
                  {foundOptions.length === 0 && <SelectFieldNoOptionsMessage />}
                  {foundOptions.map((option, index) => {
                    const isSelected = value.includes(option.value);

                    return (
                      <SelectFieldMenuItem
                        key={index}
                        onClick={() => {
                          if (isSelected) {
                            onChange?.(
                              value.filter((value) => value !== option.value)
                            );
                          } else {
                            onChange?.([...value, option.value]);
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
            </div>
          );
        }}
      </FieldBody>
    </div>
  );
};

const FormikMultiselectField = makeFormikField(MultiselectField, {
  nativeEvents: false,
});

export { MultiselectField, FormikMultiselectField };
