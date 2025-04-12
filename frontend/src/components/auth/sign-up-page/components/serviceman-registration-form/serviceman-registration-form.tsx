import classNames from 'classnames';

import styles from './styles.module.scss';
import { useState } from 'react';
import { ServicemanInviteCodeForm } from '../serviceman-invite-code-form';

type Props = React.ComponentPropsWithoutRef<'div'>;

const ServicemanRegistrationForm: React.FC<Props> = ({
  className,
  ...props
}) => {
  const [inviteCode, setInviteCode] = useState<string | null>(null);

  if (inviteCode === null) {
    return <ServicemanInviteCodeForm />;
  }

  return (
    <div {...props} className={classNames(styles.root, className)}>
      ServicemanRegistrationForm
    </div>
  );
};

export { ServicemanRegistrationForm };
