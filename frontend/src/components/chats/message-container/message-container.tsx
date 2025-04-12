import classNames from 'classnames';
import styles from './styles.module.scss';

interface Props extends React.ComponentPropsWithoutRef<'div'> {
  alignment: 'left' | 'right';
}

const MessageContainer: React.FC<Props> = ({
  alignment,
  className,
  ...props
}) => {
  return (
    <div
      {...props}
      className={classNames(styles.container, styles[alignment], className)}
    />
  );
};

export { MessageContainer };
