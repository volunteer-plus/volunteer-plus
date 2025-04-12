import { HTTP_STATUS_TO_NAME } from '@/constants/common';
import { accessTokenService } from '@/services/common/access-token';
import { isEmpty } from 'lodash';

interface ConnectionConfig {
  basePath: string;
}

type SearchObject = Record<string, unknown>;

interface RequestOptions {
  method: string;
  path: string;
  payload?: unknown;
  search?: SearchObject;
}

type RequestOptionsWithoutMethod = Omit<RequestOptions, 'method'>;

class VolunteerPlusApiError extends Error {
  constructor(status: number, message: string = 'Unknown API error') {
    super(message);
    this.status = status;
  }

  public readonly status: number;
}

class VolunteerPlusApiService extends EventTarget {
  constructor(config: ConnectionConfig) {
    super();

    this.basePath = config.basePath;
  }

  public getFullUrl(path: string): string {
    return `${this.basePath}/${path}`;
  }

  public makePostRequest<R = unknown>({
    path,
    payload,
    search,
  }: RequestOptionsWithoutMethod): Promise<R> {
    return this.makeRequest({
      method: 'POST',
      path,
      payload,
      search,
    });
  }

  public makeGetRequest<R = unknown>({
    path,
    payload,
    search,
  }: RequestOptionsWithoutMethod): Promise<R> {
    return this.makeRequest({
      method: 'GET',
      path,
      payload,
      search,
    });
  }

  public makePutRequest<R = unknown>({
    path,
    payload,
    search,
  }: RequestOptionsWithoutMethod): Promise<R> {
    return this.makeRequest({
      method: 'PUT',
      path,
      payload,
      search,
    });
  }

  public makeDeleteRequest<R = unknown>({
    path,
    payload,
    search,
  }: RequestOptionsWithoutMethod): Promise<R> {
    return this.makeRequest({
      method: 'DELETE',
      path,
      payload,
      search,
    });
  }

  private async makeRequest<R = unknown>({
    method,
    path,
    payload,
    search,
  }: RequestOptions): Promise<R> {
    const requestInit: RequestInit = {
      method,
      body: JSON.stringify(payload),
    };

    const accessToken = this.getAccessToken();

    if (accessToken) {
      requestInit.headers = {
        Authorization: `Bearer ${accessToken}`,
      };
    }

    if (payload) {
      requestInit.body = JSON.stringify(payload);
      requestInit.headers = Object.assign(requestInit.headers ?? {}, {
        'Content-Type': 'application/json',
      });
    }

    let url = this.getFullUrl(path);

    if (search && !isEmpty(search)) {
      url = this.addSearchToUrl(url, search);
    }

    const response = await fetch(url, requestInit);

    if (response.ok) {
      return (await response.json()) as R;
    }

    throw new VolunteerPlusApiError(
      response.status,
      this.getErrorMessageByHttpStatus(response.status)
    );
  }

  private addSearchToUrl(url: string, search: SearchObject) {
    const params = new URLSearchParams();

    for (const [key, value] of Object.entries(search)) {
      if (Array.isArray(value)) {
        params.set(key, value.join(','));
      } else {
        params.set(key, String(value));
      }
    }

    return `${url}?${params.toString()}`;
  }

  private getErrorMessageByHttpStatus(status: number): string {
    const statusString = status.toString();

    if (statusString in HTTP_STATUS_TO_NAME) {
      return HTTP_STATUS_TO_NAME[
        statusString as keyof typeof HTTP_STATUS_TO_NAME
      ];
    }

    return 'Unknown error';
  }

  private getAccessToken() {
    const token = accessTokenService.get();

    if (!token) {
      return null;
    }

    if (accessTokenService.isTokenExpired(token)) {
      this.dispatchEvent(new Event('accessTokenExpired'));
    }

    return token;
  }

  private basePath: string;
}

const volunteerPlusApiService = new VolunteerPlusApiService({
  basePath: import.meta.env.VITE_VOLUNTEER_PLUS_API_BASE_URL,
});

export { volunteerPlusApiService, VolunteerPlusApiError };
