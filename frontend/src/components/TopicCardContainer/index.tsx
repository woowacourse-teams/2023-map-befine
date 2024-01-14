import { Swiper, Tab } from 'map-befine-swiper';
import { styled } from 'styled-components';

import useTopicsQuery from '../../hooks/api/useTopicsQuery';
import useKeyDown from '../../hooks/useKeyDown';
import Box from '../common/Box';
import Flex from '../common/Flex';
import Space from '../common/Space';
import Text from '../common/Text';
import MediaText from '../common/Text/MediaText';
import TopicCard from '../TopicCard';

interface TopicCardContainerProps {
  url: string;
  containerTitle: string;
  containerDescription: string;
  routeWhenSeeAll: () => void;
}

function TopicCardContainer({
  url,
  containerTitle,
  containerDescription,
  routeWhenSeeAll,
}: TopicCardContainerProps) {
  const { topics, refetchTopics } = useTopicsQuery(url);
  const { elementRef, onElementKeyDown } = useKeyDown<HTMLSpanElement>();

  return (
    <section>
      <Flex $justifyContent="space-between" $alignItems="flex-end">
        <Box>
          <MediaText
            as="h2"
            color="black"
            $fontSize="extraLarge"
            $fontWeight="bold"
            tabIndex={0}
          >
            {containerTitle}
          </MediaText>
          <Space size={0} />
          <MediaText
            color="gray"
            $fontSize="default"
            $fontWeight="normal"
            tabIndex={0}
          >
            {containerDescription}
          </MediaText>
        </Box>

        <PointerText
          color="primary"
          $fontSize="small"
          $fontWeight="normal"
          tabIndex={0}
          onClick={routeWhenSeeAll}
          role="button"
          aria-label={`${containerTitle} 전체보기`}
          ref={elementRef}
          onKeyDown={onElementKeyDown}
        >
          전체 보기
        </PointerText>
      </Flex>

      <Space size={4} />

      <Swiper
        as="ul"
        width={1140}
        height={300}
        $elementsOneTab={5}
        $elementsMediaQueries={[1180, 900, 660, 320]}
        swiper
        swipeable
        $isNotTabBoxShow
      >
        {topics &&
          topics.map(
            (topic, index) =>
              index < 10 && (
                <Tab label={`${index}`} key={topic.id}>
                  <Flex>
                    <CustomSpace />
                    <TopicCard
                      cardType="default"
                      id={topic.id}
                      image={topic.image}
                      name={topic.name}
                      creator={topic.creator}
                      updatedAt={topic.updatedAt}
                      pinCount={topic.pinCount}
                      bookmarkCount={topic.bookmarkCount}
                      isInAtlas={topic.isInAtlas}
                      isBookmarked={topic.isBookmarked}
                      getTopicsFromServer={refetchTopics}
                    />
                    <CustomSpace />
                  </Flex>
                </Tab>
              ),
          )}
      </Swiper>
    </section>
  );
}

const PointerText = styled(Text)`
  cursor: pointer;
`;

const CustomSpace = styled.div`
  min-width: 10px;
  min-height: 10px;
`;

export default TopicCardContainer;
