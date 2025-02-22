import { isSameDate } from '@/helpers/common';

function formatDate(date: Date): string {
  const today = new Date();

  const tomorrow = new Date();
  tomorrow.setDate(tomorrow.getDate() + 1);

  const yesterday = new Date();
  yesterday.setDate(yesterday.getDate() - 1);

  if (isSameDate(date, today)) {
    return 'Сьогодні';
  }

  if (isSameDate(date, tomorrow)) {
    return 'Завтра';
  }

  if (isSameDate(date, yesterday)) {
    return 'Вчора';
  }

  return `${date.getDate()}.${date.getMonth()}.${date.getFullYear()}`;
}

export { formatDate };
