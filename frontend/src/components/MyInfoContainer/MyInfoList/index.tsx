import { Fragment, useEffect, useState } from 'react';
import { styled } from 'styled-components';
import { getApi } from '../../../apis/getApi';
import { MyInfoPinType, MyInfoTopicType } from '../../../types/MyInfo';
import PinCard from '../../PinCard';
import TopicCard from '../../TopicCard';

const data = [
  {
    id: 1,
    name: '준팍의 또 토픽',
    image: 'https://map-befine-official.github.io/favicon.png',
    pinCount: 3,
    bookmarkCount: 0,
    isBookmarked: false,
    updatedAt: '2023-08-14T13:15:40.15966098',
  },
  {
    id: 2,
    name: '준팍의 두번째 토픽',
    image: 'https://map-befine-official.github.io/favicon.png',
    pinCount: 5,
    bookmarkCount: 0,
    isBookmarked: false,
    updatedAt: '2023-08-14T13:15:40.15966888',
  },
  {
    id: 3,
    name: '준팍의 또 토픽',
    image: 'https://map-befine-official.github.io/favicon.png',
    pinCount: 3,
    bookmarkCount: 0,
    isBookmarked: false,
    updatedAt: '2023-08-14T13:15:40.15966098',
  },
  {
    id: 4,
    name: '준팍의 두번째 토픽',
    image: 'https://map-befine-official.github.io/favicon.png',
    pinCount: 5,
    bookmarkCount: 0,
    isBookmarked: false,
    updatedAt: '2023-08-14T13:15:40.15966888',
  },
];

const data2 = [
  {
    id: 1,
    name: '매튜의 산스장',
    address: '지번 주소',
    description: '매튜가 사랑하는 산스장',
    latitude: 37.0,
    longitude: 127.0,
  },
  {
    id: 2,
    name: '매튜의 안갈집',
    address: '지번 주소',
    description: '매튜가 두번은 안 갈 집',
    latitude: 37.0,
    longitude: 127.0,
  },
  {
    id: 3,
    name: '매튜의 산스장',
    address: '지번 주소',
    description: '매튜가 사랑하는 산스장',
    latitude: 37.0,
    longitude: 127.0,
  },
  {
    id: 4,
    name: '매튜의 안갈집',
    address: '지번 주소',
    description: '매튜가 두번은 안 갈 집',
    latitude: 37.0,
    longitude: 127.0,
  },
];

interface MyInfoListProps {
  isTopic: boolean;
}

const MyInfoList = ({ isTopic }: MyInfoListProps) => {
  const [myInfoTopics, setMyInfoTopics] = useState<MyInfoTopicType[]>([]);
  const [myInfoPins, setMyInfoPins] = useState<MyInfoPinType[]>([]);

  const getMyInfoListFromServer = async () => {
    if (isTopic) {
      const serverMyInfoTopics = await getApi<MyInfoTopicType[]>(
        'default',
        '/members/pins',
      );
      setMyInfoTopics(serverMyInfoTopics);
    }

    if (!isTopic) {
      const serverMyInfoPins = await getApi<MyInfoPinType[]>(
        'default',
        '/members/topics',
      );
      setMyInfoPins(serverMyInfoPins);
    }
  };

  useEffect(() => {
    // getMyInfoListFromServer();
  }, []);

  if (!isTopic)
    return (
      <MyInfoListWrapper>
        {data2.map((pin, index) => {
          return (
            index < 3 && (
              <Fragment key={pin.id}>
                <PinCard
                  pinId={pin.id}
                  pinTitle={pin.name}
                  pinAddress={pin.address}
                  pinDescription={pin.description}
                />
              </Fragment>
            )
          );
        })}
      </MyInfoListWrapper>
    );

  if (!data) return <></>;

  return (
    <MyInfoListWrapper>
      {data.map((topic, index) => {
        return (
          index < 3 && (
            <Fragment key={topic.id}>
              <TopicCard
                topicId={topic.id}
                topicImage={topic.image}
                topicTitle={topic.name}
                topicUpdatedAt={topic.updatedAt}
                topicPinCount={topic.pinCount}
              />
            </Fragment>
          )
        );
      })}
    </MyInfoListWrapper>
  );
};

const MyInfoListWrapper = styled.ul`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(340px, 1fr));
  grid-row-gap: ${({ theme }) => theme.spacing[5]};
`;

export default MyInfoList;
