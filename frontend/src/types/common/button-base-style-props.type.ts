import { ButtonBaseColorSchema, ButtonBaseVariant } from '@/types/common';

interface ButtonBaseStyleProps {
  colorSchema?: ButtonBaseColorSchema;
  variant?: ButtonBaseVariant;
  leftIcon?: React.ReactNode;
  rightIcon?: React.ReactNode;
}

export type { ButtonBaseStyleProps };
