import { User } from '@/types/common';
import { volunteerPlusApiService } from '../volunteer-plus-api';

class UserService {
  public async getMe(): Promise<User> {
    const user = await volunteerPlusApiService.makeGetRequest<User>({
      path: 'user-info',
    });

    user.email = 'super.admin@example.com';

    return user;
  }
}

const userService = new UserService();

export { userService };
