import { ProgressBar } from '@/components/common';
import { formatMoneyAmount } from '@/helpers/common';

import styles from './styles.module.scss';

interface Props extends React.ComponentPropsWithoutRef<'div'> {
  target: number;
  raised: number;
}

const FundraisingProgressBar: React.FC<Props> = ({
  raised,
  target,
  ...props
}) => {
  const progress = (raised / target) * 100;

  return (
    <div {...props}>
      <ProgressBar progress={progress} />
      <div className={styles.legend}>
        <div className={styles.item}>
          <div className={styles.title}>Зібрано</div>
          <div className={styles.value}>{formatMoneyAmount(raised)} грн</div>
        </div>
        <div className={styles.item}>
          <div className={styles.title}>Ціль</div>
          <div className={styles.value}>{formatMoneyAmount(target)} грн</div>
        </div>
      </div>
    </div>
  );
};

export { FundraisingProgressBar };
