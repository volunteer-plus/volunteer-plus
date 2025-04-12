import { Form, useFormikContext } from 'formik';
import { Button, FormikTextInputField } from '@/components/common';

import styles from './styles.module.scss';

const SupportForm: React.FC<React.ComponentPropsWithoutRef<typeof Form>> = (
  props
) => {
  const { isValid } = useFormikContext();

  return (
    <Form {...props}>
      <div className={styles.fields}>
        <FormikTextInputField
          name='email'
          isRequired
          label='Ваш email'
          placeholder='example@example.ua'
        />
        <FormikTextInputField
          name='amount'
          isRequired
          label='Сума вашого внеску'
          placeholder='0.00'
          units='₴'
        />
      </div>
      <div className={styles.buttons}>
        <Button type='submit' disabled={!isValid}>
          Підтримати
        </Button>
        <Button type='button' variant='outlined'>
          Про волонтера
        </Button>
      </div>
    </Form>
  );
};

export { SupportForm };
