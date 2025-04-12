import { FieldBodyVariant } from '@/types/common';
import styles from './style.module.scss';
import classNames from 'classnames';

interface Props extends React.ComponentPropsWithoutRef<'div'> {
  variant: FieldBodyVariant;
  topMargin?: boolean;
}

const FieldDescription: React.FC<Props> = ({
  variant,
  className,
  topMargin,
  ...props
}) => {
  return (
    <div
      {...props}
      className={classNames(styles.description, className, styles[variant], {
        [styles.topMargin]: topMargin,
      })}
    />
  );
};

export { FieldDescription };
