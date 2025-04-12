import { useEffect, useRef, useState } from 'react';
import classNames from 'classnames';

import { MaterialSymbol, Tooltip } from '@/components/common';

import styles from './styles.module.scss';

type Props = React.ComponentPropsWithoutRef<'div'> & {
  value?: string;
};

const SecretKeyField: React.FC<Props> = ({
  className,
  value = '',
  ...props
}) => {
  const [isShown, setIsShown] = useState(false);
  const [shouldShowCopiedTooltip, setShouldShowCopiedTooltip] = useState(false);

  const showToggleButtonRef = useRef<HTMLButtonElement>(null);
  const copyButtonRef = useRef<HTMLButtonElement>(null);

  useEffect(() => {
    if (!shouldShowCopiedTooltip) {
      return;
    }

    const timeout = setTimeout(() => {
      setShouldShowCopiedTooltip(false);
    }, 1000);

    return () => clearTimeout(timeout);
  }, [shouldShowCopiedTooltip]);

  useEffect(() => {
    if (!isShown) {
      return;
    }

    const timeout = setTimeout(() => {
      setIsShown(false);
    }, 10000);

    return () => clearTimeout(timeout);
  }, [isShown]);

  const onCopyButtonClick = () => {
    if (value) {
      navigator.clipboard.writeText(value);
    }

    setShouldShowCopiedTooltip(true);
  };

  return (
    <div {...props} className={classNames(styles.root, className)}>
      <input
        type={isShown ? 'text' : 'password'}
        className={styles.input}
        readOnly
        value={value}
      />
      <button type='button' className={styles.button} ref={showToggleButtonRef}>
        <MaterialSymbol
          onClick={() => setIsShown((prev) => !prev)}
          className={styles.icon}
        >
          {isShown ? 'visibility_off' : 'visibility'}
        </MaterialSymbol>
      </button>
      <button type='button' className={styles.button} ref={copyButtonRef}>
        <MaterialSymbol className={styles.icon} onClick={onCopyButtonClick}>
          content_copy
        </MaterialSymbol>
      </button>
      <Tooltip targetRef={showToggleButtonRef}>
        {isShown ? 'Сховати' : 'Показати'}
      </Tooltip>
      <Tooltip targetRef={copyButtonRef}>
        {shouldShowCopiedTooltip ? 'Скопійовано!' : 'Скопіювати'}
      </Tooltip>
    </div>
  );
};

export { SecretKeyField };
