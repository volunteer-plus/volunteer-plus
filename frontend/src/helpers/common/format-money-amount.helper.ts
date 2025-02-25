function formatMoneyAmount(amount: number): string {
  return `${amount.toLocaleString('uk')} грн`;
}

export { formatMoneyAmount };
