function getCoordinateCssValue(coordinate?: number | null): string {
  return `${coordinate ? coordinate.toFixed(2) : 0}px`;
}

export { getCoordinateCssValue };
