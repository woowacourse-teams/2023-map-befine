import { styled } from 'styled-components';
import Box from '../components/common/Box';
import Text from '../components/common/Text';
import { FULLSCREEN } from '../constants';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import { Suspense, lazy, useEffect, useState } from 'react';
import FavoriteNotFilledSVG from '../assets/favoriteBtn_notFilled.svg';
import TopicCardListSeleton from '../components/TopicCardList/TopicCardListSeleton';
import useToast from '../hooks/useToast';
import { TopicType } from '../types/Topic';
import { getApi } from '../apis/getApi';
import Button from '../components/common/Button';
import useNavigator from '../hooks/useNavigator';

const BookmarksList = lazy(() => import('../components/BookmarksList'));

const Bookmark = () => {
  const { width } = useSetLayoutWidth(FULLSCREEN);
  const { navbarHighlights: __ } = useSetNavbarHighlight('favorite');
  const [bookmarks, setBookmarks] = useState<TopicType[] | null>(null);
  const { showToast } = useToast();
  const { routePage } = useNavigator();

  const getBookmarksFromServer = async () => {
    try {
      const serverBookmarks = await getApi<TopicType[]>(
        'default',
        '/members/my/bookmarks',
      );
      setBookmarks(serverBookmarks);
    } catch {
      showToast('error', '로그인 후 이용해주세요.');
    }
  };

  const goToHome = () => {
    routePage('/');
  };

  useEffect(() => {
    getBookmarksFromServer();
  }, []);

  if (!bookmarks) return <></>;

  if (bookmarks.length === 0) {
    return (
      <WrapperWhenEmpty width={width}>
        <Flex $alignItems="center">
          <FavoriteNotFilledSVG />
          <Space size={1} />
          <Text color="black" $fontSize="default" $fontWeight="normal">
            버튼을 눌러 지도를 추가해보세요.
          </Text>
          <Space size={4} />
        </Flex>
        <Space size={5} />
        <Button variant="primary" onClick={goToHome}>
          메인페이지로 가기
        </Button>
      </WrapperWhenEmpty>
    );
  }

  return (
    <BookMarksWrapper>
      <section>
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

          <PointerText
            color="primary"
            $fontSize="small"
            $fontWeight="normal"
            tabIndex={2}
          >
            생선된 순
          </PointerText>
        </Flex>

        <Space size={6} />

        <Suspense fallback={<TopicCardListSeleton />}>
          <BookmarksList
            bookmarks={bookmarks}
            setTopicsFromServer={getBookmarksFromServer}
          />
        </Suspense>
      </section>
    </BookMarksWrapper>
  );
};

const WrapperWhenEmpty = styled.section<{ width: '372px' | '100vw' }>`
  width: ${({ width }) => `calc(${width} - 40px)`};
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const PointerText = styled(Text)`
  cursor: pointer;
`;

const BookMarksWrapper = styled(Box)`
  width: 70vw;
  margin: 0 auto;
`;

export default Bookmark;
