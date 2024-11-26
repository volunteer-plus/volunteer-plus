import { LiqpayCheckoutButtonVariables } from '@/types/fundraising';

type Props = LiqpayCheckoutButtonVariables;

const LiqpayCheckoutButton: React.FC<Props> = ({ data, signature }) => {
  return (
    <form
      method='POST'
      action='https://www.liqpay.ua/api/3/checkout'
      acceptCharset='utf-8'
    >
      <input type='hidden' name='data' value={data} />
      <input type='hidden' name='signature' value={signature} />
      <input type='image' src='//static.liqpay.ua/buttons/payUk.png' />
    </form>
  );
};

export { LiqpayCheckoutButton };
