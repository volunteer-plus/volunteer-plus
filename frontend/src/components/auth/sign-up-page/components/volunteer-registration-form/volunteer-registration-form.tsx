import classNames from 'classnames';

import styles from './styles.module.scss';

type Props = React.ComponentPropsWithoutRef<'div'>;

const VolunteerRegistrationForm: React.FC<Props> = ({ className, ...props }) => {
  return (
    <div {...props} className={classNames(styles.root, className)}>
      VolunteerRegistrationForm
    </div>
  );
};

export { VolunteerRegistrationForm };
