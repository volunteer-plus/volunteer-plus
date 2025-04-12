import { useState } from 'react';
import {
  makeFormikField,
  MaterialSymbol,
  TextInputField,
} from '@/components/common';

import styles from './styles.module.scss';

type Props = Omit<
  React.ComponentPropsWithoutRef<typeof TextInputField>,
  'type' | 'rightIcon'
>;

const PasswordInputField: React.FC<Props> = (props) => {
  const [isPasswordVisible, setIsPasswordVisible] = useState(false);

  const togglePasswordVisibility = () => {
    setIsPasswordVisible((prev) => !prev);
  };

  return (
    <TextInputField
      {...props}
      type={isPasswordVisible ? 'text' : 'password'}
      rightIcon={
        <button
          type='button'
          onClick={togglePasswordVisibility}
          className={styles.visibilityButton}
        >
          <MaterialSymbol>
            {isPasswordVisible ? 'visibility_off' : 'visibility'}
          </MaterialSymbol>
        </button>
      }
    />
  );
};

const FormikPasswordInputField = makeFormikField(PasswordInputField);

export { PasswordInputField, FormikPasswordInputField };
