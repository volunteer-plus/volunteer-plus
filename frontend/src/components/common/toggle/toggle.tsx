import classNames from 'classnames';
import styles from './styles.module.scss';
import { useMemo } from 'react';

interface Props extends React.ComponentPropsWithoutRef<'label'> {
  inputProps?: React.ComponentPropsWithoutRef<'input'>;
  isChecked?: boolean;
  onInputChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const Toggle: React.FC<Props> = ({
  inputProps: inputPropsProp,
  isChecked,
  onInputChange,
  className,
  ...props
}) => {
  const inputProps = useMemo<React.ComponentPropsWithoutRef<'input'>>(() => {
    return {
      ...inputPropsProp,
      checked: isChecked,
      onChange: onInputChange,
      type: 'checkbox',
      className: styles.input,
    };
  }, [inputPropsProp, isChecked, onInputChange]);

  return (
    <label {...props} className={classNames(styles.label, className)}>
      <input {...inputProps} />
      <div className={styles.toggle}>
        <div className={styles.lever} />
      </div>
    </label>
  );
};

export { Toggle };
