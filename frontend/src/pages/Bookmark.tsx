import { styled } from 'styled-components';
import Box from '../components/common/Box';
import Text from '../components/common/Text';
import { FULLSCREEN } from '../constants';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import { Suspense, lazy } from 'react';
import TopicCardContainerSkeleton from '../components/Skeletons/TopicListSkeleton';
import useNavigator from '../hooks/useNavigator';
import FavoriteNotFilledSVG from '../assets/favoriteBtn_notFilled.svg';
import { setFullScreenResponsive } from '../constants/responsive';

const TopicCardList = lazy(() => import('../components/TopicCardList'));

const Bookmark = () => {
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
          <Text
            color="black"
            $fontSize="extraLarge"
            $fontWeight="bold"
            tabIndex={0}
          >
            즐겨찾기
          </Text>
          <Space size={0} />
          <Text
            color="gray"
            $fontSize="default"
            $fontWeight="normal"
            tabIndex={1}
          >
            즐겨찾기한 지도들을 한 눈에 보세요.
          </Text>
        </Box>
      </Flex>

      <Space size={6} />

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
    </Wrapper>
  );
};

const Wrapper = styled.article`
  width: 1036px;
  margin: 0 auto;

  ${setFullScreenResponsive()}
`;

export default Bookmark;
