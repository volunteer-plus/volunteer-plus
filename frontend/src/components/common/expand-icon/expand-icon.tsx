import classNames from 'classnames';
import styles from './styles.module.scss';
import { MaterialSymbol } from '../material-symbol';

interface Props extends React.ComponentPropsWithoutRef<typeof MaterialSymbol> {
  isExpanded: boolean;
}

const ExpandIcon: React.FC<Props> = ({ isExpanded, className, ...props }) => {
  return (
    <MaterialSymbol
      className={classNames(
        styles.icon,
        isExpanded ? styles.expanded : styles.collapsed,
        className
      )}
      {...props}
    >
      chevron_forward
    </MaterialSymbol>
  );
};

export { ExpandIcon };
