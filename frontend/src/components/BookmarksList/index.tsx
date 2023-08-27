import { Fragment, useEffect, useState } from 'react';
import { styled } from 'styled-components';
import TopicCard from '../TopicCard';
import { TopicCardProps } from '../../types/Topic';
import useGet from '../../apiHooks/useGet';
import Flex from '../common/Flex';
import FavoriteNotFilledSVG from '../../assets/favoriteBtn_notFilled.svg';
import Space from '../common/Space';
import Text from '../common/Text';
import Button from '../common/Button';

interface BookmarksListProps {
  goToHome: () => void;
}

const BookmarksList = ({ goToHome }: BookmarksListProps) => {
  const [bookmarks, setBookmarks] = useState<TopicCardProps[] | null>(null);
  const { fetchGet } = useGet();

  const getBookmarksFromServer = async () => {
    fetchGet<TopicCardProps[]>(
      '/members/my/bookmarks',
      '로그인 후 이용해주세요.',
      (response) => {
        setBookmarks(response);
      },
    );
  };

  useEffect(() => {
    getBookmarksFromServer();
  }, []);

  if (!bookmarks) return <></>;

  if (bookmarks.length === 0) {
    return (
      <WrapperWhenEmpty>
        <Flex $alignItems="center">
          <FavoriteNotFilledSVG />
          <Space size={1} />
          <Text color="black" $fontSize="default" $fontWeight="normal">
            버튼을 눌러 지도를 즐겨찾기에 추가해보세요.
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
    <Wrapper>
      {bookmarks.map((topic) => (
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
            setTopicsFromServer={getBookmarksFromServer}
          />
        </Fragment>
      ))}
    </Wrapper>
  );
};

const WrapperWhenEmpty = styled.section`
  height: 460px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const Wrapper = styled.ul`
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
`;

export default BookmarksList;
