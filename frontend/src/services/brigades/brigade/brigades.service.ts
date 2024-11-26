import { volunteerPlusApiService } from '@/services/common/volunteer-plus-api.service';
import { CreateOrUpdateBrigadesPayload } from './types';

class BrigadeService {
  async createOrUpdateBrigades(
    payload: CreateOrUpdateBrigadesPayload
  ): Promise<unknown> {
    return await volunteerPlusApiService.makePostRequest(
      'brigade/create-or-update',
      payload
    );
  }
}

const brigadeService = new BrigadeService();

export { brigadeService };
