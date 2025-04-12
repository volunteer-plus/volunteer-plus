import { CounterBox } from '@/components/common';
import styles from './styles.module.scss';

const DonationsStatistics: React.FC = () => {
  return (
    <div className={styles.container}>
      <h2 className={styles.heading}>Статистика пожертв</h2>
      <div className={styles.counters}>
        <CounterBox
          counter={100}
          title='Найчастіша пожертва'
          subtitle='за 30  днів'
          units='грн'
        />
        <CounterBox
          counter={1356}
          title='Середня пожертва'
          subtitle='за 30  днів'
          units='грн'
        />
        <CounterBox
          counter={12671}
          title='Кількість пожертв'
          subtitle='за 30  днів'
        />
      </div>
    </div>
  );
};

export { DonationsStatistics };
