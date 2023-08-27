import { styled } from 'styled-components';
import Box from '../components/common/Box';
import Text from '../components/common/Text';
import { FULLSCREEN } from '../constants';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import { Suspense, lazy } from 'react';
import TopicCardListSkeleton from '../components/TopicCardList/TopicCardListSkeleton';
import useNavigator from '../hooks/useNavigator';

const BookmarksList = lazy(() => import('../components/BookmarksList'));

const Bookmark = () => {
  const { routePage } = useNavigator();
  useSetLayoutWidth(FULLSCREEN);
  useSetNavbarHighlight('favorite');

  const goToHome = () => {
    routePage('/');
  };

  return (
    <BookMarksWrapper>
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

      <Suspense fallback={<TopicCardListSkeleton />}>
        <BookmarksList goToHome={goToHome} />
      </Suspense>
    </BookMarksWrapper>
  );
};

const BookMarksWrapper = styled.section`
  width: 1036px;
  margin: 0 auto;
`;

export default Bookmark;
