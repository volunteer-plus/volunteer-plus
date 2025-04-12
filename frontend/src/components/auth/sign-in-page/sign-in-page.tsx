import { Form, Formik } from 'formik';

import { Yup } from '@/yup';
import {
  AuthForm,
  AuthFormActions,
  AuthFormBody,
  AuthFormTitle,
  AuthPageLayout,
} from '@/components/auth';
import {
  Button,
  FormikPasswordInputField,
  FormikTextInputField,
  Link,
} from '@/components/common';
import { getUserHomepage } from '@/helpers/user';
import { PublicOnly } from '@/components/auth/public-only';

import styles from './styles.module.scss';
import { useAppDispatch, useAppSelector } from '@/hooks/store';
import { login } from '@/slices/user';

type FormValues = {
  email: string;
  password: string;
};

const INITIAL_FORM_VALUES: FormValues = {
  email: '',
  password: '',
};

const FORM_VALIDATION_SCHEMA = Yup.object({
  email: Yup.string().required().label('Email'),
  password: Yup.string().required().label('Пароль'),
});

const BareSignInPage: React.FC = () => {
  const dispatch = useAppDispatch();

  const { isLoginFailed } = useAppSelector((state) => state.user);

  const onSubmit = async (values: FormValues) => {
    await dispatch(login(values));
  };

  return (
    <AuthPageLayout>
      <Formik
        initialValues={INITIAL_FORM_VALUES}
        onSubmit={onSubmit}
        validationSchema={FORM_VALIDATION_SCHEMA}
      >
        {({ isValid, isSubmitting }) => {
          return (
            <AuthForm as={Form}>
              <AuthFormTitle>Увійти</AuthFormTitle>
              <AuthFormBody>
                <FormikTextInputField
                  name='email'
                  label='Email'
                  isRequired
                  placeholder='example@example.ua'
                  variant={isLoginFailed ? 'failure' : undefined}
                  description={
                    isLoginFailed ? 'Невірний email або пароль' : undefined
                  }
                />
                <FormikPasswordInputField
                  name='password'
                  label='Пароль'
                  isRequired
                />
              </AuthFormBody>
              <AuthFormActions>
                <Button
                  className={styles.submitButton}
                  type='submit'
                  disabled={!isValid || isSubmitting}
                >
                  Увійти
                </Button>
                <Link to='/sign-up' className={styles.signUpLink}>
                  Зареєструватися
                </Link>
              </AuthFormActions>
            </AuthForm>
          );
        }}
      </Formik>
    </AuthPageLayout>
  );
};

const SignInPage = PublicOnly(BareSignInPage, {
  getRedirectUrl: (user) => getUserHomepage(user),
});

export { SignInPage };
