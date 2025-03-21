import { volunteerPlusApiService } from '@/services/common/volunteer-plus-api/volunteer-plus-api.service';
import { CreateDonationLiqpayOrderPayload } from './types';
import { LiqpayCheckoutButtonVariables } from '@/types/fundraising';

class FundraisingService {
  async createDonationLiqPayOrder(
    payload: CreateDonationLiqpayOrderPayload
  ): Promise<LiqpayCheckoutButtonVariables> {
    return await volunteerPlusApiService.makePostRequest({
      path: 'liq-pay/create',
      payload,
    });
  }
}

const fundraisingService = new FundraisingService();

export { fundraisingService };
