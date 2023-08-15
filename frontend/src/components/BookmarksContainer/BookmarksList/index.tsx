import { Fragment, useEffect, useState } from 'react';
import { styled } from 'styled-components';
import { getApi } from '../../../apis/getApi';
import { BookmarksType } from '../../../types/Bookmarks';
import Box from '../../common/Box';
import TopicCard from '../../TopicCard';

const data = [
  {
    id: 1,
    name: '준팍의 또 토픽',
    image: 'https://map-befine-official.github.io/favicon.png',
    pinCount: 3,
    bookmarkCount: 100,
    isBookmarked: true,
    updatedAt: '2023-08-14T13:15:33.563088131',
  },
  {
    id: 2,
    name: '준팍의 두번째 토픽',
    image: 'https://map-befine-official.github.io/favicon.png',
    pinCount: 5,
    bookmarkCount: 150,
    isBookmarked: true,
    updatedAt: '2023-08-14T13:15:33.563105931',
  },
  {
    id: 3,
    name: '준팍의 또 토픽',
    image: 'https://map-befine-official.github.io/favicon.png',
    pinCount: 3,
    bookmarkCount: 100,
    isBookmarked: true,
    updatedAt: '2023-08-14T13:15:33.563088131',
  },
  {
    id: 4,
    name: '준팍의 두번째 토픽',
    image: 'https://map-befine-official.github.io/favicon.png',
    pinCount: 5,
    bookmarkCount: 150,
    isBookmarked: true,
    updatedAt: '2023-08-14T13:15:33.563105931',
  },
  {
    id: 5,
    name: '준팍의 또 토픽',
    image: 'https://map-befine-official.github.io/favicon.png',
    pinCount: 3,
    bookmarkCount: 100,
    isBookmarked: true,
    updatedAt: '2023-08-14T13:15:33.563088131',
  },
  {
    id: 6,
    name: '준팍의 두번째 토픽',
    image: 'https://map-befine-official.github.io/favicon.png',
    pinCount: 5,
    bookmarkCount: 150,
    isBookmarked: true,
    updatedAt: '2023-08-14T13:15:33.563105931',
  },
  {
    id: 7,
    name: '준팍의 또 토픽',
    image: 'https://map-befine-official.github.io/favicon.png',
    pinCount: 3,
    bookmarkCount: 100,
    isBookmarked: true,
    updatedAt: '2023-08-14T13:15:33.563088131',
  },
  {
    id: 8,
    name: '준팍의 두번째 토픽',
    image: 'https://map-befine-official.github.io/favicon.png',
    pinCount: 5,
    bookmarkCount: 150,
    isBookmarked: true,
    updatedAt: '2023-08-14T13:15:33.563105931',
  },
  {
    id: 9,
    name: '준팍의 또 토픽',
    image: 'https://map-befine-official.github.io/favicon.png',
    pinCount: 3,
    bookmarkCount: 100,
    isBookmarked: true,
    updatedAt: '2023-08-14T13:15:33.563088131',
  },
  {
    id: 10,
    name: '준팍의 두번째 토픽',
    image: 'https://map-befine-official.github.io/favicon.png',
    pinCount: 5,
    bookmarkCount: 150,
    isBookmarked: true,
    updatedAt: '2023-08-14T13:15:33.563105931',
  },
  {
    id: 11,
    name: '준팍의 또 토픽',
    image: 'https://map-befine-official.github.io/favicon.png',
    pinCount: 3,
    bookmarkCount: 100,
    isBookmarked: true,
    updatedAt: '2023-08-14T13:15:33.563088131',
  },
  {
    id: 12,
    name: '준팍의 두번째 토픽',
    image: 'https://map-befine-official.github.io/favicon.png',
    pinCount: 5,
    bookmarkCount: 150,
    isBookmarked: true,
    updatedAt: '2023-08-14T13:15:33.563105931',
  },
];

const BookmarksList = () => {
  const [bookmarks, setBookmarks] = useState<BookmarksType[]>([]);

  const getBookmarksFromServer = async () => {
    const serverBookmarks = await getApi<BookmarksType[]>('default', '/bookmarks');
    setBookmarks(serverBookmarks)
  };

  useEffect(() => {
    getBookmarksFromServer();
  }, []);

  if (!data) return <></>;

  return (
    <BookMarksListWrapper>
      {data.map((topic) => (
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
