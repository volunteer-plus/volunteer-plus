import { formatDate, formatTime } from '@/helpers/common';

function formatDateAndTime(date: Date) {
  return `${formatDate(date)}, ${formatTime(date)}`;
}

export { formatDateAndTime };
