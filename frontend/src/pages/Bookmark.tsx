import { styled } from 'styled-components';

import useGetBookmarks from '../apiHooks/new/useGetBookmarks';
import { getBookmarks } from '../apis/new';
import FavoriteNotFilledSVG from '../assets/favoriteBtn_notFilled.svg';
import Box from '../components/common/Box';
import Button from '../components/common/Button';
import Flex from '../components/common/Flex';
import Grid from '../components/common/Grid';
import Space from '../components/common/Space';
import MediaSpace from '../components/common/Space/MediaSpace';
import Text from '../components/common/Text';
import MediaText from '../components/common/Text/MediaText';
import TopicListSkeleton from '../components/Skeletons/TopicListSkeleton';
import TopicCard from '../components/TopicCard';
import { ARIA_FOCUS, FULLSCREEN } from '../constants';
import useNavigator from '../hooks/useNavigator';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';

function Bookmark() {
  useSetLayoutWidth(FULLSCREEN);
  useSetNavbarHighlight('favorite');

  const { routingHandlers } = useNavigator();
  const { isLoading, bookmarks: topics } = useGetBookmarks();

  if (isLoading)
    return (
      <>
        <Wrapper>
          <Space size={5} />
          <TopicListSkeleton />
        </Wrapper>
      </>
    );

  return (
    <Wrapper>
      <Space size={5} />
      <Flex $justifyContent="space-between" $alignItems="flex-end">
        <Box>
          <MediaText
            as="h2"
            color="black"
            $fontSize="extraLarge"
            $fontWeight="bold"
            tabIndex={ARIA_FOCUS}
            aria-label="즐겨찾기 페이지 입니다. 즐겨찾기한 지도들을 확인할 수 있습니다."
          >
            즐겨찾기
          </MediaText>
          <Space size={0} />
          <MediaText color="gray" $fontSize="default" $fontWeight="normal">
            즐겨찾기한 지도들을 한 눈에 보세요.
          </MediaText>
        </Box>
      </Flex>

      <MediaSpace size={6} />

      {topics ? (
        <TopicsWrapper>
          <Grid
            rows="auto"
            columns={5}
            gap={20}
            width="100%"
            $mediaQueries={[
              [1180, 4],
              [900, 3],
              [660, 2],
              [320, 1],
            ]}
          >
            {topics.map((topic) => (
              <ul key={topic.id}>
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
                  getTopicsFromServer={getBookmarks}
                />
              </ul>
            ))}
          </Grid>
        </TopicsWrapper>
      ) : (
        <EmptyWrapper>
          <Flex $alignItems="center">
            <FavoriteNotFilledSVG />
            <Space size={1} />
            <Text color="black" $fontSize="default" $fontWeight="normal">
              버튼으로 지도를 즐겨찾기에 담아보세요.
            </Text>
            <Space size={4} />
          </Flex>
          <Space size={5} />
          <Button variant="primary" onClick={routingHandlers.home}>
            메인페이지로 가기
          </Button>
        </EmptyWrapper>
      )}
      <Space size={8} />
    </Wrapper>
  );
}

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

const TopicsWrapper = styled.section`
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
`;

export default Bookmark;
