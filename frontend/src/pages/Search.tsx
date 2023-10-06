import styled from 'styled-components';
import { setFullScreenResponsive } from '../constants/responsive';
import Space from '../components/common/Space';
import SearchBar from '../components/SearchBar/SearchBar';
import { Fragment, useEffect, useState } from 'react';
import Flex from '../components/common/Flex';
import Box from '../components/common/Box';
import Text from '../components/common/Text';
import TopicCard from '../components/TopicCard';
import { TopicCardProps } from '../types/Topic';
import { useLocation } from 'react-router-dom';
import useGet from '../apiHooks/useGet';

const Search = () => {
  const { fetchGet } = useGet();

  const [originalTopics, setOriginalTopics] = useState<TopicCardProps[] | null>(
    null,
  );
  const [displayedTopics, setDisplayedTopics] = useState<
    TopicCardProps[] | null
  >(null);
  const searchQuery = decodeURIComponent(useLocation().search.substring(1));

  const getTopicsFromServer = async () => {
    fetchGet<TopicCardProps[]>(
      '/topics',
      '지도를 가져오는데 실패했습니다.',
      (response) => {
        setOriginalTopics(response);
        const searchResult = response.filter((topic) =>
          topic.name.includes(searchQuery),
        );
        setDisplayedTopics(searchResult);
      },
    );
  };

  useEffect(() => {
    getTopicsFromServer();
  }, []);

  useEffect(() => {
    if (originalTopics) {
      const searchResult = originalTopics.filter((topic) =>
        topic.name.includes(searchQuery),
      );
      setDisplayedTopics(searchResult);
    }
  }, [searchQuery]);

  return (
    <Wrapper>
      <Space size={1} />
      <SearchBar />
      <Space size={1} />
      <Flex $justifyContent="space-between" $alignItems="flex-end">
        <Box>
          <Text
            color="black"
            $fontSize="extraLarge"
            $fontWeight="bold"
            tabIndex={0}
          >
            찾았을 지도?
          </Text>
          <Space size={0} />
          <Text
            color="gray"
            $fontSize="default"
            $fontWeight="normal"
            tabIndex={1}
          >
            검색한 지도를 확인해보세요.
          </Text>
        </Box>
      </Flex>
      <Space size={6} />

      {displayedTopics?.length === 0 ? (
        // 검색 결과가 없을 때의 UI
        <EmptyWrapper>
          <Flex $alignItems="center">
            <Space size={1} />
            <Text color="black" $fontSize="default" $fontWeight="normal">
              '{searchQuery}'에 대한
              {'검색 결과가 없습니다.'}
            </Text>
            <Space size={4} />
          </Flex>
          <Space size={5} />
        </EmptyWrapper>
      ) : (
        <CardListWrapper>
          {displayedTopics?.map((topic) => (
            <Fragment key={topic.id}>
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
              />
            </Fragment>
          ))}
        </CardListWrapper>
      )}
    </Wrapper>
  );
};

export default Search;

const Wrapper = styled.article`
  width: 1036px;
  margin: 0 auto;
  position: relative;

  ${setFullScreenResponsive()}
`;

const CardListWrapper = styled.ul`
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
`;

const EmptyWrapper = styled.section`
  height: 240px;
  display: flex;
  flex-direction: column;
  align-items: center;
`;
