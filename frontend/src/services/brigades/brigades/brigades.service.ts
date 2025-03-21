import { volunteerPlusApiService } from '@/services/common/volunteer-plus-api/volunteer-plus-api.service';
import {
  CreateOrUpdateBrigadesPayload,
  GetBrigadesOptions,
  ListBrigade,
} from './types';
import { pickBy } from 'lodash';

class BrigadesService {
  async createOrUpdateBrigades(
    payload: CreateOrUpdateBrigadesPayload
  ): Promise<unknown> {
    return await volunteerPlusApiService.makePostRequest({
      path: 'brigade/create-or-update',
      payload,
    });
  }

  async getBrigades({ ids }: GetBrigadesOptions = {}): Promise<ListBrigade[]> {
    return await volunteerPlusApiService.makeGetRequest({
      path: 'brigades',
      search: pickBy(
        {
          ids,
        },
        Boolean
      ),
    });
  }
}

const brigadesService = new BrigadesService();

export { brigadesService };
