import { lazy, Suspense } from 'react';
import { styled } from 'styled-components';

import FavoriteNotFilledSVG from '../assets/favoriteBtn_notFilled.svg';
import Box from '../components/common/Box';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import MediaSpace from '../components/common/Space/MediaSpace';
import MediaText from '../components/common/Text/MediaText';
import TopicCardContainerSkeleton from '../components/Skeletons/TopicListSkeleton';
import { ARIA_FOCUS, FULLSCREEN } from '../constants';
import useNavigator from '../hooks/useNavigator';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';

const TopicCardList = lazy(() => import('../components/TopicCardList'));

function Bookmark() {
  const { routePage } = useNavigator();
  useSetLayoutWidth(FULLSCREEN);
  useSetNavbarHighlight('favorite');

  const goToHome = () => {
    routePage('/');
  };

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

      <Suspense fallback={<TopicCardContainerSkeleton />}>
        <TopicCardList
          url="/members/my/bookmarks"
          errorMessage="로그인 후 이용해주세요."
          commentWhenEmpty="버튼으로 지도를 즐겨찾기에 담아보세요."
          pageCommentWhenEmpty="메인페이지로 가기"
          routePage={goToHome}
        >
          <FavoriteNotFilledSVG />
        </TopicCardList>
      </Suspense>

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

export default Bookmark;
