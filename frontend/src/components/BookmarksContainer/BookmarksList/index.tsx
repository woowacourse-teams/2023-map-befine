import { Fragment, useEffect, useState } from 'react';
import { styled } from 'styled-components';
import { getApi } from '../../../apis/getApi';
import { BookmarksType } from '../../../types/Bookmarks';
import TopicCard from '../../TopicCard';

const BookmarksList = () => {
  const [bookmarks, setBookmarks] = useState<BookmarksType[]>([]);

  const getBookmarksFromServer = async () => {
    const serverBookmarks = await getApi<BookmarksType[]>(
      'default',
      '/members/my/bookmarks',
    );
    setBookmarks(serverBookmarks);
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
            topicId={topic.id}
            topicImage={topic.image}
            topicTitle={topic.name}
            topicUpdatedAt={topic.updatedAt}
            topicPinCount={topic.pinCount}
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
