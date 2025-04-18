import { RoomMessage } from './room-message.type';
import { RoomUser } from './room-user.type';

interface Room {
  id: number;
  name: string;
  deleted: boolean;
  messages: RoomMessage[];
  users: RoomUser[];
}

export type { Room };
