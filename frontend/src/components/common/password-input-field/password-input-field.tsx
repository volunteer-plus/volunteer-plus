import { useState } from 'react';
import { TextInputField } from '@/components/common';

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
          <span className='material-symbols-outlined'>
            {isPasswordVisible ? 'visibility_off' : 'visibility'}
          </span>
        </button>
      }
    />
  );
};

export { PasswordInputField };
