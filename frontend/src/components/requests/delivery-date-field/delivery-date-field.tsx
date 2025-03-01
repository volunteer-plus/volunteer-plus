import {
  Checkbox,
  CheckboxAndLabelContainer,
  FieldDescription,
  FieldLabel,
  makeFormikField,
  TextInputField,
} from '@/components/common';
import { FieldBodyVariant } from '@/types/common';

import styles from './styles.module.scss';
import { DELIVERY_DATE_FIELD_ASAP_VALUE } from './constants';

type Props = {
  description?: React.ReactNode;
  variant?: FieldBodyVariant;
  isDisabled?: boolean;
  label?: React.ReactNode;
  isRequired?: boolean;
  value?: string | null;
  onChange?: (value: string | null) => void;
} & React.ComponentPropsWithoutRef<'div'>;

const DeliveryDateField: React.FC<Props> = ({
  description,
  variant = 'default',
  isDisabled,
  label,
  isRequired,
  value,
  onChange,
  ...props
}) => {
  const isAsap = value === DELIVERY_DATE_FIELD_ASAP_VALUE;

  return (
    <div {...props}>
      {label && (
        <FieldLabel bottomMargin isRequired={isRequired}>
          Крайній терімн поставки
        </FieldLabel>
      )}
      <div className={styles.inputAndCheckbox}>
        <TextInputField
          placeholder='Вкажіть термін'
          disabled={isDisabled || isAsap}
          variant={variant}
          type='date'
          className={styles.dateField}
          value={isAsap ? '' : value ?? ''}
          onChange={(e) => {
            onChange?.(e.target.value);
          }}
        />
        <CheckboxAndLabelContainer>
          <Checkbox
            checked={isAsap}
            onChange={() => {
              onChange?.(isAsap ? null : DELIVERY_DATE_FIELD_ASAP_VALUE);
            }}
          />
          <FieldLabel className={styles.asapLabel}>Якомога швидше</FieldLabel>
        </CheckboxAndLabelContainer>
      </div>
      {description && (
        <FieldDescription variant={variant} topMargin>
          {description}
        </FieldDescription>
      )}
    </div>
  );
};

const FormikDeliveryDateField = makeFormikField(DeliveryDateField, {
  nativeEvents: false,
});

export { DeliveryDateField, FormikDeliveryDateField };
