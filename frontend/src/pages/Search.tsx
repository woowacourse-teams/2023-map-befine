import { Fragment, useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import styled from 'styled-components';

import useGet from '../apiHooks/useGet';
import Box from '../components/common/Box';
import Flex from '../components/common/Flex';
import Grid from '../components/common/Grid';
import Space from '../components/common/Space';
import MediaText from '../components/common/Text/MediaText';
import SearchBar from '../components/SearchBar/SearchBar';
import TopicCard from '../components/TopicCard';
import { FULLSCREEN } from '../constants';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import { TopicCardProps } from '../types/Topic';

function Search() {
  const { fetchGet } = useGet();
  useSetLayoutWidth(FULLSCREEN);
  useSetNavbarHighlight('none');

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
      <Space size={5} />
      <Flex $justifyContent="space-between" $alignItems="flex-end">
        <Box>
          <MediaText
            as="h2"
            color="black"
            $fontSize="extraLarge"
            $fontWeight="bold"
            tabIndex={0}
          >
            찾았을 지도?
          </MediaText>
          <Space size={0} />
          <MediaText
            color="gray"
            $fontSize="default"
            $fontWeight="normal"
            tabIndex={1}
          >
            {`${searchQuery} 검색 결과입니다.`}
          </MediaText>
        </Box>
      </Flex>
      <Space size={6} />

      {displayedTopics?.length === 0 ? (
        // 검색 결과가 없을 때의 UI
        <EmptyWrapper>
          <Flex $alignItems="center">
            <Space size={1} />
            <MediaText color="black" $fontSize="default" $fontWeight="normal">
              {`'${searchQuery}'에 대한 검색 결과가 없습니다.`}
            </MediaText>
            <Space size={4} />
          </Flex>
          <Space size={5} />
        </EmptyWrapper>
      ) : (
        <Grid
          as="ul"
          rows="auto"
          columns={5}
          gap={20}
          $mediaQueries={[
            [1180, 4],
            [900, 3],
            [660, 2],
            [320, 1],
          ]}
        >
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
        </Grid>
      )}
      <Space size={8} />
    </Wrapper>
  );
}

export default Search;

const Wrapper = styled.section`
  width: 1140px;
  margin: 0 auto;
  position: relative;

  @media (max-width: 1180px) {
    width: 100%;
  }
`;

const EmptyWrapper = styled.section`
  height: 240px;
  display: flex;
  flex-direction: column;
  align-items: center;
`;
