import { AuthPageLayout } from '@/components/auth';
import { useState } from 'react';

import styles from './styles.module.scss';
import { SignUpRole } from './enums';
import {
  RoleSelectionForm,
  ServicemanRegistrationForm,
  VolunteerRegistrationForm,
} from './components';

const SignUpPage: React.FC = () => {
  const [role, setRole] = useState<SignUpRole | null>(null);

  return (
    <AuthPageLayout>
      <h1 className={styles.heading}>Створити профіль</h1>
      {role === null && (
        <RoleSelectionForm onSubmit={(values) => setRole(values.role)} />
      )}
      {role === SignUpRole.SERVICEMAN && <ServicemanRegistrationForm />}
      {role === SignUpRole.VOLUNTEER && <VolunteerRegistrationForm />}
    </AuthPageLayout>
  );
};

export { SignUpPage };
