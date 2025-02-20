import { FieldBodyVariant } from '@/types/common';
import { Field, FieldConfig, FieldProps } from 'formik';

type BaseProps = JSX.IntrinsicAttributes & {
  variant?: FieldBodyVariant;
  description?: React.ReactNode;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  onBlur?: (...args: any[]) => void;
};

function makeFormikField<P extends BaseProps>(
  Component: React.ComponentType<P>,
  { nativeEvents = true }: { nativeEvents?: boolean } = {}
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
        {({ field, meta, form }: FieldProps) => {
          const componentProps: P = {
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
            ...(props as any),
            variant: meta.touched && meta.error ? 'failure' : variant,
            description: meta.touched && meta.error ? meta.error : description,
          };

          if (nativeEvents) {
            Object.assign(componentProps, field);
          } else {
            Object.assign(componentProps, {
              value: field.value,
              onChange: (value: unknown) => form.setFieldValue(name, value),
              onBlur: () => form.setFieldTouched(name, true),
            });
          }

          return <Component {...componentProps} />;
        }}
      </Field>
    );
  };
}

export { makeFormikField };
