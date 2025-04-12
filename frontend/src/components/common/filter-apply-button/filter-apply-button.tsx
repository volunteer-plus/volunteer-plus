import { Button } from '../button';

import styles from './styles.module.scss';

type Props = Omit<React.ComponentPropsWithoutRef<typeof Button>, 'children'>;

const FilterApplyButton: React.FC<Props> = () => {
  return <Button className={styles.button}>Застосувати</Button>;
};

export { FilterApplyButton };
