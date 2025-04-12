import classNames from 'classnames';
import { Form, Formik } from 'formik';
import { Button, FormikSelectField, Link } from '@/components/common';
import { SelectOption } from '@/types/common';
import { Yup } from '@/yup';

import { SignUpRole } from '../../enums';

import styles from './styles.module.scss';
import {
  RoleSelectionFormValues,
  ValidatedRoleSelectionFormValues,
} from './types';

type Props = Omit<React.ComponentPropsWithoutRef<typeof Form>, 'onSubmit'> & {
  onSubmit?: (values: ValidatedRoleSelectionFormValues) => void;
};

const FORM_INITIAL_VALUES: RoleSelectionFormValues = {
  role: null,
};

const FORM_VALIDATION_SCHEMA = Yup.object({
  role: Yup.string().oneOf(Object.values(SignUpRole)).required().label('Роль'),
});

const ROLES_OPTIONS: SelectOption[] = [
  {
    value: SignUpRole.SERVICEMAN,
    label: 'Військовослужбовець',
  },
  {
    value: SignUpRole.VOLUNTEER,
    label: 'Волонтер',
  },
];

const RoleSelectionForm: React.FC<Props> = ({
  className,
  onSubmit,
  ...props
}) => {
  return (
    <Formik
      initialValues={FORM_INITIAL_VALUES}
      onSubmit={async (values) => {
        onSubmit?.(values as ValidatedRoleSelectionFormValues);
      }}
      validationSchema={FORM_VALIDATION_SCHEMA}
    >
      {({ isValid }) => {
        return (
          <Form className={classNames(styles.form, className)} {...props}>
            <FormikSelectField
              name='role'
              options={ROLES_OPTIONS}
              isRequired
              label='Створити профіль як'
              placeholder='Оберіть роль'
            />
            <div className={styles.actions}>
              <Button
                type='submit'
                disabled={!isValid}
                className={styles.submitButton}
              >
                Далі
              </Button>
              <div className={styles.signInLinkWrapper}>
                <Link to='/sign-in'>Увійти</Link>
              </div>
            </div>
          </Form>
        );
      }}
    </Formik>
  );
};

export { RoleSelectionForm };
