import { forwardRef } from 'react';
import Flex from './common/Flex';

const Map = (props: any, ref: any) => {
  return (
    <Flex
      flex="1"
      id="map"
      ref={ref}
      style={{
        height: '100vh',
        width: '100%',
      }}
    />
  );
};

export default forwardRef(Map);
