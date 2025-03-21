import { jwtDecode } from 'jwt-decode';

const LOCAL_STORAGE_KEY = 'accessToken';

class AccessTokenService {
  get() {
    return localStorage.getItem(LOCAL_STORAGE_KEY);
  }

  set(token: string) {
    localStorage.setItem(LOCAL_STORAGE_KEY, token);
  }

  clear() {
    localStorage.removeItem(LOCAL_STORAGE_KEY);
  }

  isTokenExpired(token: string) {
    const decodedToken = jwtDecode(token);

    if (!decodedToken) {
      return true;
    }

    if (typeof decodedToken.exp === 'undefined') {
      return false;
    }

    const expirationDate = new Date(decodedToken.exp * 1000);

    return expirationDate < new Date();
  }
}

const accessTokenService = new AccessTokenService();

export { accessTokenService };
