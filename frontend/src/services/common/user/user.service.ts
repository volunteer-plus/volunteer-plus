import { User } from '@/types/common';
import { volunteerPlusApiService } from '../volunteer-plus-api';
import { UserRole } from '@/enums/common';

class UserService {
  public async getMe(): Promise<User> {
    const user = await volunteerPlusApiService.makeGetRequest<User>({
      path: 'user-info',
    });

    const urlParams = new URLSearchParams(window.location.search);

    user.role = user.role ?? urlParams.get('userRole') ?? UserRole.VOLUNTEER;

    return user;
  }
}

const userService = new UserService();

export { userService };
