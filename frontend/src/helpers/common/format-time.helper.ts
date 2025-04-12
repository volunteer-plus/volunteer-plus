function getTwoDigitsNumber(number: number): string {
  if (number < 10) {
    return '0' + number;
  }

  return number.toString();
}

function formatTime(date: Date) {
  return `${getTwoDigitsNumber(date.getHours())}:${getTwoDigitsNumber(
    date.getMinutes()
  )}`;
}

export { formatTime };
