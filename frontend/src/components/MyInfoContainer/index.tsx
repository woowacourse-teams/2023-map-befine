import Flex from '../common/Flex';
import Text from '../common/Text';
import Box from '../common/Box';
import Space from '../common/Space';
import { lazy, Suspense } from 'react';
import TopicCardContainerSkeleton from '../TopicCardContainer/TopicCardContainerSkeleton';

const MyInfoList = lazy(() => import('../MyInfoList'));

interface MyInfoContainerProps {
  containerTitle: string;
  containerDescription: string;
}

const MyInfoContainer = ({
  containerTitle,
  containerDescription,
}: MyInfoContainerProps) => (
  <section>
    <Flex $justifyContent="space-between" $alignItems="flex-end">
      <Box>
        <Text
          color="black"
          $fontSize="extraLarge"
          $fontWeight="bold"
          tabIndex={0}
        >
          {containerTitle}
        </Text>
        <Space size={0} />
        <Text
          color="gray"
          $fontSize="default"
          $fontWeight="normal"
          tabIndex={1}
        >
          {containerDescription}
        </Text>
      </Box>
    </Flex>

    <Space size={4} />

    <Suspense fallback={<TopicCardContainerSkeleton />}>
      <MyInfoList />
    </Suspense>
  </section>
);

export default MyInfoContainer;
