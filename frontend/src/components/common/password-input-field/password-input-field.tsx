import { useState } from 'react';
import { TextInputField } from '../text-input-field';

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
        <button type='button' onClick={togglePasswordVisibility}>
          {isPasswordVisible ? 'H' : 'S'}
        </button>
      }
    />
  );
};

export { PasswordInputField };
