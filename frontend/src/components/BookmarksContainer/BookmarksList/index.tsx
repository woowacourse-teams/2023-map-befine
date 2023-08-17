import { Fragment, useEffect, useState } from 'react';
import { styled } from 'styled-components';
import { getApi } from '../../../apis/getApi';
import TopicCard from '../../TopicCard';
import { TopicType } from '../../../types/Topic';
import useToast from '../../../hooks/useToast';

const BookmarksList = () => {
  const [bookmarks, setBookmarks] = useState<TopicType[]>([]);
  const { showToast } = useToast();

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

  useEffect(() => {
    getBookmarksFromServer();
  }, []);

  if (!bookmarks) return <></>;

  return (
    <BookMarksListWrapper>
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
    </BookMarksListWrapper>
  );
};

const BookMarksListWrapper = styled.ul`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(340px, 1fr));
  grid-row-gap: ${({ theme }) => theme.spacing[5]};
`;

export default BookmarksList;
