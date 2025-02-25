import styles from './styles.module.scss';

interface Props {
  progress: number;
}

const ProgressBar: React.FC<Props> = ({ progress }) => {
  return (
    <div className={styles.container}>
      <div
        className={styles.progress}
        style={{
          width: `${progress}%`,
        }}
      />
    </div>
  );
};

export { ProgressBar };
