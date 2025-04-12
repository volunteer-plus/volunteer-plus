import classNames from 'classnames';
import styles from './styles.module.scss';

const CheckboxAndLabelContainer: React.FC<
  React.ComponentPropsWithRef<'div'>
> = ({ className, ...props }) => {
  return <div {...props} className={classNames(styles.container, className)} />;
};

export { CheckboxAndLabelContainer };
