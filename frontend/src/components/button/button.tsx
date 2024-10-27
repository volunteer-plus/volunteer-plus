import { ButtonBase } from '@/components';
import { ButtonBaseStyleProps } from '@/types';

type Props = React.ComponentPropsWithoutRef<'button'> & ButtonBaseStyleProps;

const Button: React.FC<Props> = ({ colorSchema, variant, ...props }) => {
  return (
    <ButtonBase
      elementType='button'
      elementProps={{
        type: 'button',
        ...props,
      }}
      colorSchema={colorSchema}
      variant={variant}
    />
  );
};

export { Button };
