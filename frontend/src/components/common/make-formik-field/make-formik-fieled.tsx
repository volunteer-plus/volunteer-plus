import { FieldBodyVariant } from '@/types/common';
import { Field, FieldConfig, FieldProps } from 'formik';

type BaseProps = JSX.IntrinsicAttributes & {
  variant?: FieldBodyVariant;
  description?: React.ReactNode;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  onBlur?: (...args: any[]) => void;
};

function makeFormikField<P extends BaseProps>(
  Component: React.ComponentType<P>
) {
  return ({
    name,
    variant,
    description,
    onBlur,
    ...props
  }: P & Pick<FieldConfig, 'name'>) => {
    return (
      <Field name={name} onBlur={onBlur}>
        {({ field, meta }: FieldProps) => {
          const componentProps: P = {
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
            ...(props as any),
            variant: meta.touched && meta.error ? 'failure' : variant,
            description: meta.touched && meta.error ? meta.error : description,
            ...field,
          };

          return <Component {...componentProps} />;
        }}
      </Field>
    );
  };
}

export { makeFormikField };
