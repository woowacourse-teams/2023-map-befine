import { styled } from 'styled-components';
import Flex from '../common/Flex';
import Text from '../common/Text';
import Box from '../common/Box';
import Space from '../common/Space';
import { lazy, Suspense } from 'react';
import TopicCardListSeleton from '../TopicCardList/TopicCardListSeleton';
import useKeyDown from '../../hooks/useKeyDown';

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
}: TopicListContainerProps) => {
  const { elementRef, onElementKeyDown } = useKeyDown<HTMLSpanElement>();

  return (
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
            tabIndex={0}
          >
            {containerDescription}
          </Text>
        </Box>

        <PointerText
          color="primary"
          $fontSize="small"
          $fontWeight="normal"
          tabIndex={0}
          onClick={routeWhenSeeAll}
          aria-label="인기 급상승할 지도 전체보기 버튼"
          ref={elementRef}
          onKeyDown={onElementKeyDown}
        >
          전체 보기
        </PointerText>
      </Flex>

      <Space size={4} />

      <Suspense fallback={<TopicCardListSeleton />}>
        <TopicCardList />
      </Suspense>
    </section>
  );
};

const PointerText = styled(Text)`
  cursor: pointer;
`;

export default TopicListContainer;
