import { User } from '@/types/common';

class UserService {
  public async getMe(): Promise<User> {
    return {
      email: 'super.admin@example.com',
      firstName: 'John',
      lastName: 'Doe',
    };
  }
}

const userService = new UserService();

export { userService };
