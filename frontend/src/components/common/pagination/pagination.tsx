import { MaterialSymbol } from '@/components/common';
import { Link } from 'react-router-dom';
import classNames from 'classnames';

import { ButtonAnchor } from './components';
import styles from './styles.module.scss';
import { useMemo } from 'react';

interface Props extends React.ComponentPropsWithoutRef<'div'> {
  totalPages: number;
  /**
   * Current page number. 1-based.
   */
  currentPage: number;
  getPageUrl: (page: number) => string;
}

const PAGES_AROUND = 2;
const FIRST_PAGE = 1;

const Pagination: React.FC<Props> = ({
  totalPages,
  currentPage,
  className,
  getPageUrl,
  ...props
}) => {
  const pagesToRender = useMemo<Array<number | null>>(() => {
    const pagesAround = [currentPage];

    while (pagesAround.length < PAGES_AROUND * 2 + 1) {
      let wasAdded = false;

      if (pagesAround[0] > FIRST_PAGE) {
        pagesAround.unshift(pagesAround[0] - 1);
        wasAdded = true;
      }

      if (pagesAround[pagesAround.length - 1] < totalPages) {
        pagesAround.push(pagesAround[pagesAround.length - 1] + 1);
        wasAdded = true;
      }

      if (!wasAdded) {
        break;
      }
    }

    const firstPageAround = pagesAround[0];
    const lastPageAround = pagesAround[pagesAround.length - 1];

    const pages: Array<number | null> = [...pagesAround];

    const isFirstPageShown =
      FIRST_PAGE >= firstPageAround && FIRST_PAGE <= lastPageAround;
    const isLastPageShown =
      totalPages >= firstPageAround && totalPages <= lastPageAround;

    if (!isFirstPageShown) {
      pages.unshift(FIRST_PAGE, null);
    }

    if (!isLastPageShown) {
      pages.push(null, totalPages);
    }

    return pages;
  }, [currentPage, totalPages]);

  return (
    <nav {...props} className={classNames(styles.nav, className)}>
      <ul className={styles.list}>
        <Link to='/' className={styles.chevronAnchor} rel='prev'>
          <MaterialSymbol className={styles.chevronIcon}>
            chevron_left
          </MaterialSymbol>
        </Link>
        {pagesToRender.map((page, index) => {
          return (
            <ButtonAnchor
              key={index}
              to={page ? getPageUrl(page) : undefined}
              isActive={page === currentPage}
            >
              {page ?? '...'}
            </ButtonAnchor>
          );
        })}
        <Link to='/' className={styles.chevronAnchor} rel='next'>
          <MaterialSymbol className={styles.chevronIcon}>
            chevron_right
          </MaterialSymbol>
        </Link>
      </ul>
    </nav>
  );
};

export { Pagination };
