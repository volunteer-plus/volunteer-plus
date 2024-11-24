import { HTTP_STATUS_TO_NAME } from '@/constants/common';

interface ConnectionConfig {
  basePath: string;
}

type VolunteerPlusApiErrorName = 'unknown';

class VolunteerPlusApiError extends Error {
  constructor(
    message: string = 'Unknown API error',
    name: VolunteerPlusApiErrorName = 'unknown'
  ) {
    super(message);
    this.name = name;
  }

  public name: VolunteerPlusApiErrorName;
}

class VolunteerPlusApiService {
  constructor(config: ConnectionConfig) {
    this.basePath = config.basePath;
  }

  public getFullUrl(path: string): string {
    return `${this.basePath}/${path}`;
  }

  public makePostRequest<R = unknown>(
    path: string,
    payload?: unknown
  ): Promise<R> {
    return this.makeRequest('POST', path, payload);
  }

  public makeGetRequest<R = unknown>(path: string): Promise<R> {
    return this.makeRequest('GET', path);
  }

  public makePutRequest<R = unknown>(
    path: string,
    payload?: unknown
  ): Promise<R> {
    return this.makeRequest('PUT', path, payload);
  }

  public makeDeleteRequest<R = unknown>(path: string): Promise<R> {
    return this.makeRequest('DELETE', path);
  }

  private async makeRequest<R = unknown>(
    method: string,
    path: string,
    payload?: unknown
  ): Promise<R> {
    const requestInit: RequestInit = {
      method,
      body: JSON.stringify(payload),
    };

    if (payload) {
      requestInit.body = JSON.stringify(payload);
      requestInit.headers = Object.assign(requestInit.headers ?? {}, {
        'Content-Type': 'application/json',
      });
    }

    const response = await fetch(this.getFullUrl(path), requestInit);

    if (response.ok) {
      return (await response.json()) as R;
    }

    throw new VolunteerPlusApiError(
      this.getErrorMessageByHttpStatus(response.status)
    );
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

  private basePath: string;
}

const volunteerPlusApiService = new VolunteerPlusApiService({
  basePath: import.meta.env.VITE_VOLUNTEER_PLUS_API_BASE_URL,
});

export { volunteerPlusApiService };
