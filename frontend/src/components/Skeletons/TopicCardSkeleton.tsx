import { keyframes, styled } from 'styled-components';

import Box from '../common/Box';
import Space from '../common/Space';
import SkeletonBox from './common/SkeletonBox';

function TopicCardSkeleton() {
  return (
    <Box>
      <SkeletonBox width="100%" $maxWidth={212} ratio="1.6 / 1" />
      <Space size={1} />
      <SkeletonBox width={212} height={25} />
      <Space size={5} />
      <SkeletonBox width={100} height={25} />
      <Space size={1} />
      <SkeletonBox width={212} height={46} />
    </Box>
  );
}

export default TopicCardSkeleton;
