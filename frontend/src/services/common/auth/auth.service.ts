import {
  VolunteerPlusApiError,
  volunteerPlusApiService,
} from '../volunteer-plus-api.service';
import { LoginCredentials, LoginResponse } from './types';

class InvalidCredentialsError extends Error {
  constructor() {
    super('Invalid credentials');
  }
}

class AuthService {
  public async login(credentials: LoginCredentials): Promise<LoginResponse> {
    try {
      return await volunteerPlusApiService.makePostRequest<LoginResponse>(
        'login',
        credentials
      );
    } catch (error) {
      if (error instanceof VolunteerPlusApiError) {
        throw new InvalidCredentialsError();
      }

      throw error;
    }
  }
}

const authService = new AuthService();

export { authService, InvalidCredentialsError };
