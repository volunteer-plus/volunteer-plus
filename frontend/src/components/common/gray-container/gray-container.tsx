import classNames from 'classnames';

import styles from './styles.module.scss';

interface Props extends React.ComponentPropsWithoutRef<'div'> {
  isUnderTabs?: boolean;
  noPadding?: boolean;
}

const GrayContainer: React.FC<Props> = ({
  className,
  isUnderTabs = false,
  noPadding = false,
  ...props
}) => {
  return (
    <div
      {...props}
      className={classNames(className, styles.container, {
        [styles.underTabs]: isUnderTabs,
        [styles.noPadding]: noPadding,
      })}
    />
  );
};

export { GrayContainer };
