import { MyBrigadeMilitaryPersonnel } from './my-brigade-military-personnel.type';
interface MyBrigade {
  id: number;
  createDate: string;
  updateDate: string;
  regimentCode: string;
  name: string;
  branch: string;
  role: string;
  partOf: string;
  websiteLink: string;
  currentCommander: string;
  description: string;
  militaryPersonnel: MyBrigadeMilitaryPersonnel[];
}

export type { MyBrigade };
