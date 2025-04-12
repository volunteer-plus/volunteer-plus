import { pickBy } from 'lodash';
import { volunteerPlusApiService } from '@/services/common/volunteer-plus-api/volunteer-plus-api.service';
import {
  CreateOrUpdateBrigadesPayload,
  GetBrigadesOptions,
  ListBrigade,
  MyBrigade,
  BrigadeInvite,
  CreateBrigadeInvitesPayload,
  GetBrigadeInvitesPayload,
} from './types';

class BrigadesService {
  async createOrUpdateBrigades(
    payload: CreateOrUpdateBrigadesPayload
  ): Promise<unknown> {
    return await volunteerPlusApiService.makePostRequest({
      path: 'brigade/create-or-update',
      payload: {
        brigades: payload,
      },
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

  async getBrigadeCodes(): Promise<string[]> {
    return await volunteerPlusApiService.makeGetRequest({
      path: 'brigades-codes',
    });
  }

  async getMyBrigade(): Promise<MyBrigade> {
    const brigades = await volunteerPlusApiService.makeGetRequest<MyBrigade[]>({
      path: 'brigades',
      search: {
        ids: [1],
      },
    });

    return brigades[0];
  }

  async getBrigadeInvites({
    regimentCode,
  }: GetBrigadeInvitesPayload): Promise<BrigadeInvite[]> {
    return await volunteerPlusApiService.makeGetRequest<BrigadeInvite[]>({
      path: 'add-requests',
      search: {
        regimentCode,
      },
    });
  }

  async createBrigadeInvites({
    brigadeRegimentCode,
    count,
  }: CreateBrigadeInvitesPayload): Promise<BrigadeInvite[]> {
    return await volunteerPlusApiService.makePostRequest<BrigadeInvite[]>({
      path: 'add-request/generate',
      search: {
        regimentCode: brigadeRegimentCode,
        amount: count,
      },
    });
  }
}

const brigadesService = new BrigadesService();

export { brigadesService };
