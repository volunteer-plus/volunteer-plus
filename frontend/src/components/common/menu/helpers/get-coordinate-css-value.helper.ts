function getCoordinateCssValue(coordinate?: number | null): string {
  return `${coordinate ?? 0}px`;
}

export { getCoordinateCssValue };
