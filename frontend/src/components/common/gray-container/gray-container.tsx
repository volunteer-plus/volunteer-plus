import classNames from 'classnames';

import styles from './styles.module.scss';

interface Props extends React.ComponentPropsWithoutRef<'div'> {
  isUnderTabs?: boolean;
}

const GrayContainer: React.FC<Props> = ({
  isUnderTabs,
  className,
  ...props
}) => {
  return (
    <div
      {...props}
      className={classNames(className, styles.container, {
        [styles.underTabs]: isUnderTabs,
      })}
    />
  );
};

export { GrayContainer };
