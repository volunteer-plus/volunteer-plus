import { ProgressTag, RightChevronButton } from '@/components/common';

import styles from './styles.module.scss';

// TODO: Remove progress and calculate it inside the component
type Props = {
  id: React.ReactNode;
  title: React.ReactNode;
  status: React.ReactNode;
  progress: number | null;
};

const FundraisingSimpleListItem: React.FC<Props> = ({
  id,
  title,
  status,
  progress,
}) => {
  return (
    <>
      <div className={styles.text}>№{id}</div>
      <div className={styles.text}>{title}</div>
      <div>
        <ProgressTag size='medium' progress={progress}>
          {status}
        </ProgressTag>
      </div>
      <div>
        <RightChevronButton size='20px' />
      </div>
    </>
  );
};

export { FundraisingSimpleListItem };
