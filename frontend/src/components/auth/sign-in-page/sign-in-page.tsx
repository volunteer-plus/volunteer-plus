import {
  AuthForm,
  AuthFormActions,
  AuthFormBody,
  AuthFormTitle,
  AuthPageLayout,
} from '@/components/auth';
import {
  Button,
  Link,
  PasswordInputField,
  TextInputField,
} from '@/components/common';

import styles from './styles.module.scss';

const SignInPage: React.FC = () => {
  return (
    <AuthPageLayout>
      <AuthForm>
        <AuthFormTitle>Увійти</AuthFormTitle>
        <AuthFormBody>
          <TextInputField
            label='Email'
            isRequired
            placeholder='example@example.ua'
          />
          <PasswordInputField label='Пароль' isRequired />
        </AuthFormBody>
        <AuthFormActions>
          <Button className={styles.submitButton} type='submit'>
            Увійти
          </Button>
          <Link to='/sign-up' className={styles.signUpLink}>
            Зареєструватися
          </Link>
        </AuthFormActions>
      </AuthForm>
    </AuthPageLayout>
  );
};

export { SignInPage };
