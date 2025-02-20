import { SelectOption } from '@/types/common';
import { ExportFormat } from '../enums';
import { EXPORT_FORMAT_TO_LABEL } from './export-format-to-label.constant';

const EXPORT_FORMAT_OPTIONS: SelectOption[] = Object.values(ExportFormat).map(
  (value) => ({
    value,
    label: EXPORT_FORMAT_TO_LABEL[value],
  })
);

export { EXPORT_FORMAT_OPTIONS };
