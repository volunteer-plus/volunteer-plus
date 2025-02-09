import classNames from 'classnames';

interface Props
  extends Omit<React.ComponentPropsWithoutRef<'span'>, 'children'> {
  variant?: 'outlined';
  children: string;
}

const MaterialSymbol: React.FC<Props> = ({
  variant = 'outlined',
  className,
  ...props
}) => {
  return (
    <span
      {...props}
      className={classNames(`material-symbols-${variant}`, className)}
    />
  );
};

export { MaterialSymbol };
