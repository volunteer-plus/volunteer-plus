import classNames from 'classnames';
import styles from './styles.module.scss';

interface Props extends React.ComponentPropsWithoutRef<'span'> {
  isExpanded: boolean;
}

const ExpandIcon: React.FC<Props> = ({ isExpanded, className, ...props }) => {
  return (
    <span
      {...props}
      className={classNames(
        'material-symbols-outlined',
        styles.icon,
        isExpanded ? styles.expanded : styles.collapsed,
        className
      )}
    >
      chevron_forward
    </span>
  );
};

export { ExpandIcon };
