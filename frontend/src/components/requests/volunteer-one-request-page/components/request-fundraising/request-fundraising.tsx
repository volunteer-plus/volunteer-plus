import classNames from 'classnames';

import styles from './styles.module.scss';
import {
  FundraisingSimpleList,
  FundraisingSimpleListItem,
} from '@/components/fundraising';
import { Button } from '@/components/common';

type Props = React.ComponentPropsWithoutRef<'div'>;

const RequestFundraising: React.FC<Props> = ({ className, ...props }) => {
  return (
    <div {...props} className={classNames(styles.root, className)}>
      <FundraisingSimpleList>
        <FundraisingSimpleListItem
          id='1234'
          progress={0.4}
          status='Іде збір'
          title='Бронежелети'
        />
        <FundraisingSimpleListItem
          id='1234'
          progress={1}
          status='Збір завершено'
          title='Бронежелети'
        />
        <FundraisingSimpleListItem
          id='1234'
          progress={null}
          status='Завантажено звіт'
          title='Бронежелети'
        />
      </FundraisingSimpleList>
      <Button variant='outlined' colorSchema='olive' className={styles.button}>
        Відкрити збір
      </Button>
    </div>
  );
};

export { RequestFundraising };
