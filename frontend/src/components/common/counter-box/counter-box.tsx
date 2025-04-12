import { useAnimatedCounter } from '@/hooks/common';

import styles from './styles.module.scss';

interface Props {
  title: React.ReactNode;
  subtitle: React.ReactNode;
  counter: number;
  units?: React.ReactNode;
}

const CounterBox: React.FC<Props> = ({ counter, subtitle, title, units }) => {
  const animatedCounter = useAnimatedCounter({
    value: counter,
    framerateLimit: 25,
  });

  return (
    <div className={styles.box}>
      <div>
        <h3 className={styles.title}>{title}</h3>
        <h4 className={styles.subtitle}>{subtitle}</h4>
      </div>
      <div>
        <span className={styles.counter}>{animatedCounter}</span>
        <span className={styles.units}>{units}</span>
      </div>
    </div>
  );
};

export { CounterBox };
