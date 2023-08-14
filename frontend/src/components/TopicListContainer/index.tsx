import { styled } from 'styled-components';
import Flex from '../common/Flex';
import Text from '../common/Text';
import Box from '../common/Box';
import { lazy, Suspense } from 'react';
import TopicCardListSeleton from '../TopicCardList/TopicCardListSeleton';

const TopicCardList = lazy(() => import('../TopicCardList'));

interface TopicListContainerProps {
  containerTitle: string;
  containerDescription: string;
  routeWhenSeeAll: () => void;
}

const TopicListContainer = ({
  containerTitle,
  containerDescription,
  routeWhenSeeAll,
}: TopicListContainerProps) => (
  <section>
    <Flex $justifyContent="space-between" $alignItems="flex-end">
      <Box>
        <Text color="black" $fontSize="large" $fontWeight="bold" tabIndex={0}>
          {containerTitle}
        </Text>
        <Text
          color="gray"
          $fontSize="default"
          $fontWeight="normal"
          tabIndex={1}
        >
          {containerDescription}
        </Text>
      </Box>
      <PointerText
        color="primary"
        $fontSize="small"
        $fontWeight="normal"
        tabIndex={2}
        onClick={routeWhenSeeAll}
      >
        전체 보기
      </PointerText>
    </Flex>
    <Suspense fallback={<TopicCardListSeleton />}>
      <TopicCardList />
    </Suspense>
  </section>
);

const PointerText = styled(Text)`
  cursor: pointer;
`;

export default TopicListContainer;
