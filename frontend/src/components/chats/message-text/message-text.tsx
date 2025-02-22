import classNames from 'classnames';

import styles from './styles.module.scss';

interface Props extends Omit<React.ComponentPropsWithRef<'div'>, 'children'> {
  children: string;
}

const MessageText: React.FC<Props> = ({ children, className }) => {
  return (
    <div className={classNames(styles.text, className)}>
      {children.split('\n').map((line) => (
        <p>{line}</p>
      ))}
    </div>
  );
};

export { MessageText };
