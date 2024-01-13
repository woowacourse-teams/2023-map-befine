import { styled } from 'styled-components';

import useGetBestTopics from '../apiHooks/new/useGetBestTopics';
import { getBestTopics } from '../apis/new';
import Box from '../components/common/Box';
import Flex from '../components/common/Flex';
import Grid from '../components/common/Grid';
import Space from '../components/common/Space';
import MediaSpace from '../components/common/Space/MediaSpace';
import MediaText from '../components/common/Text/MediaText';
import SkeletonBox from '../components/Skeletons/common/SkeletonBox';
import TopicListSkeleton from '../components/Skeletons/TopicListSkeleton';
import TopicCard from '../components/TopicCard';
import { ARIA_FOCUS, FULLSCREEN } from '../constants';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';

function SeeAllTopics() {
  useSetLayoutWidth(FULLSCREEN);
  useSetNavbarHighlight('home');

  const { isLoading, bestTopics: topics } = useGetBestTopics();

  if (isLoading)
    return (
      <>
        <Wrapper>
          <Space size={5} />

          <SkeletonBox width={160} height={32} />
          <Space size={4} />

          <Space size={5} />
          <TopicListSkeleton />
        </Wrapper>
      </>
    );

  return (
    <Wrapper>
      <Space size={5} />
      <MediaText
        as="h2"
        color="black"
        $fontSize="extraLarge"
        $fontWeight="bold"
        tabIndex={ARIA_FOCUS}
        aria-label="인기 급상승할 지도 전체보기 페이지 입니다."
      >
        인기 급상승할 지도?
      </MediaText>

      <MediaSpace size={6} />

      {topics && (
        <Flex as="section" $flexWrap="wrap" $gap="20px">
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
                  getTopicsFromServer={getBestTopics}
                />
              </ul>
            ))}
          </Grid>
        </Flex>
      )}

      <Space size={8} />
    </Wrapper>
  );
}

const Wrapper = styled(Box)`
  width: 1140px;
  margin: 0 auto;
  position: relative;

  @media (max-width: 1180px) {
    width: 100%;
  }
`;

export default SeeAllTopics;
