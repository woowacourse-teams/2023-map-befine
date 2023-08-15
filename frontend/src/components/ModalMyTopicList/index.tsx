import { Fragment, useEffect, useState } from 'react';
import { styled } from 'styled-components';
import { getApi } from '../../apis/getApi';
import { ModalMyTopicType } from '../../types/Topic';
import ModalTopicCard from '../ModalTopicCard';

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

const ModalMyTopicList = () => {
  const [myTopics, setMyTopics] = useState<ModalMyTopicType[]>([]);

  const getMyTopicFromServer = async () => {
    const serverMyTopic = await getApi<ModalMyTopicType[]>(
      'default',
      '/bookmarks',
    );
    setMyTopics(myTopics);
  };

//   useEffect(() => {
//     getMyTopicFromServer();
//   }, []);

  if (!data) return <></>;

  return (
    <ModalMyTopicListWrapper>
      {data.map((topic) => (
        <Fragment key={topic.id}>
          <ModalTopicCard
            topicId={topic.id}
            topicImage={topic.image}
            topicTitle={topic.name}
            topicUpdatedAt={topic.updatedAt}
            topicPinCount={topic.pinCount}
          />
        </Fragment>
      ))}
    </ModalMyTopicListWrapper>
  );
};

const ModalMyTopicListWrapper = styled.ul`
   display: grid;
  grid-template-columns: repeat(auto-fit, minmax(340px, 1fr));
  grid-row-gap: ${({ theme }) => theme.spacing[5]};
`;

export default ModalMyTopicList;
