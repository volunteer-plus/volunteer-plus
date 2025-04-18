import { ListRoom } from './list-room.type';
import { RoomUser } from './room-user.type';

type ListDirectMessagesRoom = Omit<ListRoom, 'users' | 'name'> & {
  user: RoomUser;
};

export type { ListDirectMessagesRoom };
