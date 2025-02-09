import { Bars } from 'react-loader-spinner';

interface Props {
  size: string;
}

const BarsLoader: React.FC<Props> = ({ size }) => {
  return (
    <Bars
      height={size}
      width={size}
      color='var(--color-olive-300)'
      ariaLabel='bars-loading'
      visible={true}
    />
  );
};

export { BarsLoader };
