function getFullName(data: { firstName: string; lastName: string }): string {
  return `${data.firstName} ${data.lastName}`;
}

export { getFullName };
