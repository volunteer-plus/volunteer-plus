import { ExportFormat } from '../enums';

const EXPORT_FORMAT_TO_LABEL: Record<ExportFormat, string> = {
  [ExportFormat.PDF]: 'PDF',
  [ExportFormat.XLSX]: 'Excel',
  [ExportFormat.CSV]: 'CSV',
};

export { EXPORT_FORMAT_TO_LABEL };
