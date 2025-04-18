import { RoomUser } from './room-user.type';

interface ListRoom {
  id: number;
  name: string;
  users: RoomUser[];
  newMessagesCount: number;
}

export type { ListRoom };
