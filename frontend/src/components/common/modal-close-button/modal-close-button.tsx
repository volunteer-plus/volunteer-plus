import classNames from 'classnames';

import styles from './styles.module.scss';

const ModalCloseButton: React.FC<
  Omit<React.ComponentPropsWithoutRef<'button'>, 'type' | 'children'>
> = ({ className, ...props }) => {
  return (
    <button
      {...props}
      className={classNames(styles.button, className)}
      type='button'
    >
      <span className='material-symbols-outlined'>close</span>
    </button>
  );
};

export { ModalCloseButton };
