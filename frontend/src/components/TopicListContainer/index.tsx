import { styled } from 'styled-components';
import Flex from '../common/Flex';
import Text from '../common/Text';
import Box from '../common/Box';
import Space from '../common/Space';
import { Fragment, useEffect, useState } from 'react';
import { TopicCardProps } from '../../types/Topic';
import useKeyDown from '../../hooks/useKeyDown';
import TopicCard from '../TopicCard';
import useGet from '../../apiHooks/useGet';

interface TopicListContainerProps {
  url: string;
  containerTitle: string;
  containerDescription: string;
  routeWhenSeeAll: () => void;
}

const TopicListContainer = ({
  url,
  containerTitle,
  containerDescription,
  routeWhenSeeAll,
}: TopicListContainerProps) => {
  const [topics, setTopics] = useState<TopicCardProps[] | null>(null);
  const { elementRef, onElementKeyDown } = useKeyDown<HTMLSpanElement>();
  const { fetchGet } = useGet();

  const setTopicsFromServer = async () => {
    await fetchGet<TopicCardProps[]>(
      url,
      '지도를 가져오는데 실패했습니다. 잠시 후 다시 시도해주세요.',
      (response) => {
        setTopics(response);
      },
    );
  };

  useEffect(() => {
    setTopicsFromServer();
  }, []);

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
          aria-label={`${containerTitle} 전체보기 버튼`}
          ref={elementRef}
          onKeyDown={onElementKeyDown}
        >
          전체 보기
        </PointerText>
      </Flex>

      <Space size={4} />

      <TopicsWrapper>
        {topics &&
          topics.map((topic, index) => {
            return (
              index < 6 && (
                <Fragment key={topic.id}>
                  <TopicCard
                    id={topic.id}
                    image={topic.image}
                    name={topic.name}
                    creator={topic.creator}
                    updatedAt={topic.updatedAt}
                    pinCount={topic.pinCount}
                    bookmarkCount={topic.bookmarkCount}
                    isInAtlas={topic.isInAtlas}
                    isBookmarked={topic.isBookmarked}
                    setTopicsFromServer={setTopicsFromServer}
                  />
                </Fragment>
              )
            );
          })}
      </TopicsWrapper>
    </section>
  );
};

const PointerText = styled(Text)`
  cursor: pointer;
`;

const TopicsWrapper = styled.ul`
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  height: 300px;
  overflow: hidden;
`;

export default TopicListContainer;
