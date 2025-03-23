import classNames from 'classnames';
import { isRouteErrorResponse, Link, useRouteError } from 'react-router-dom';
import { ButtonBase, MaterialSymbol } from '@/components/common';

import styles from './styles.module.scss';

type Props = React.ComponentPropsWithoutRef<'div'>;

const ErrorPage: React.FC<Props> = ({ className, ...props }) => {
  const error = useRouteError();

  let title = 'Упс... Виникла помилка';

  let text =
    'Щось пішло не так. Спробуйте оновити сторінку або зверніться до адміністратора.';

  if (isRouteErrorResponse(error)) {
    if (error.status === 404) {
      title = 'Сторінку не знайдено';
      text = 'Можливо, ви ввели неправильну адресу або сторінка була видалена.';
    } else {
      title += ` (код ${error.status})`;
      text = error.data;
    }
  } else if (error instanceof Error) {
    title += ` (${error.name})`;
    text = error.message;
    text += error.stack;
  }

  return (
    <div {...props} className={classNames(styles.root, className)}>
      <div className={styles.content}>
        <h1 className={styles.title}>{title}</h1>
        <p className={styles.text}>{text}</p>
        <ButtonBase
          elementType={Link}
          elementProps={{
            to: '/',
            children: 'На головну',
            className: styles.button,
          }}
        />
      </div>
      <MaterialSymbol className={styles.icon}>error</MaterialSymbol>
    </div>
  );
};

export { ErrorPage };
