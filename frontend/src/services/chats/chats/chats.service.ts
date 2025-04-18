import { volunteerPlusApiService } from '@/services/common/volunteer-plus-api';
import { GetRoomOptions, GetRoomsOptions, ListRoom, Room } from './types';
import { ListDirectMessagesRoom } from './types/list-direct-messages-room.type';

class ChatsService {
  async getRooms({ userId }: GetRoomsOptions): Promise<ListRoom[]> {
    const rooms = await volunteerPlusApiService.makeGetRequest<Room[]>({
      path: 'rooms',
      search: {
        userId,
      },
    });

    return rooms.map<ListRoom>((room) => {
      return {
        id: room.id,
        name: room.name,
        deleted: room.deleted,
        users: room.users,
        newMessagesCount: room.messages.length,
      };
    });
  }

  async getDirectMessagesRooms({
    userId,
  }: GetRoomsOptions): Promise<ListDirectMessagesRoom[]> {
    const rooms = await this.getRooms({ userId });

    return rooms
      .filter((room) => room.users.length === 2)
      .map((room) => {
        return {
          id: room.id,
          user: room.users[0].id === userId ? room.users[1] : room.users[0],
          newMessagesCount: room.newMessagesCount,
        };
      });
  }

  async getRoom({ roomId }: GetRoomOptions): Promise<Room> {
    return await volunteerPlusApiService.makeGetRequest<Room>({
      path: 'room',
      search: {
        conversationRoomId: roomId,
      },
    });
  }
}

const chatsService = new ChatsService();

export { chatsService };
