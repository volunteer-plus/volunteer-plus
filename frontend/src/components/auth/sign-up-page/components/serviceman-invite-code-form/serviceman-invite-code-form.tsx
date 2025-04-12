import classNames from 'classnames';
import { Link } from 'react-router-dom';

import styles from './styles.module.scss';
import { Form, Formik } from 'formik';
import {
  Button,
  ButtonBase,
  FormikPasswordInputField,
} from '@/components/common';
import { Yup } from '@/yup';

import { ServicemanInviteCodeFormValues } from './types';

type Props = React.ComponentPropsWithoutRef<'div'> & {
  onSubmit?: (values: ServicemanInviteCodeFormValues) => void;
};

const FORM_INITIAL_VALUES: ServicemanInviteCodeFormValues = {
  inviteCode: '',
};

const FORM_VALIDATION_SCHEMA = Yup.object({
  inviteCode: Yup.string().required().label('Код'),
});

const ServicemanInviteCodeForm: React.FC<Props> = ({
  className,
  onSubmit,
  ...props
}) => {
  return (
    <Formik
      initialValues={FORM_INITIAL_VALUES}
      onSubmit={(values) => onSubmit?.(values)}
      validationSchema={FORM_VALIDATION_SCHEMA}
    >
      {({ isValid }) => {
        return (
          <Form className={styles.form}>
            <FormikPasswordInputField
              name='inviteCode'
              label='Код від адміністратора бригади'
              placeholder='XXXXXXXXX'
              isRequired
            />
            <Button
              type='submit'
              className={styles.submitButton}
              disabled={!isValid}
            >
              Далі
            </Button>
            <ButtonBase
              elementType={Link}
              variant='outlined'
              elementProps={{
                to: '/',
                children: 'Cкасувати',
              }}
            />
          </Form>
        );
      }}
    </Formik>
  );
};

export { ServicemanInviteCodeForm };
