import Space from '../components/common/Space';
import Flex from '../components/common/Flex';
import PinPreview from '../components/PinPreview';
import TopicInfo from '../components/TopicInfo';
import { useEffect, useState } from 'react';
import { TopicInfoType } from '../types/Topic';
import { useNavigate, useParams } from 'react-router-dom';
import theme from '../themes';
import PinDetail from './PinDetail';

const topicData = {
  id: '1',
  name: '점심 뭐먹지',
  description: '점심을 뭐먹을지 고민하면서 만든 지도입니다.',
  pinCount: 3,
  updatedAt: '2023-07-19',
  pins: [
    {
      id: '1',
      name: '오또상스시',
      address: '서울특별시 선릉역 주변',
      description: '초밥 맛집입니다. 점심시간에는 웨이팅 좀 있어요.',
      latitude: '37.12345',
      longitude: '-122.54321',
    },
    {
      id: '2',
      name: '잇쇼우',
      address: '서울특별시 선릉역 주변',
      description: '돈까스 맛있어요. 모밀도 맛있습니다.',
      latitude: '37.12345',
      longitude: '-122.54321',
    },
    {
      id: '3',
      name: '오또상스시',
      address: '서울특별시 선릉역 주변',
      description: '초밥 맛집입니다. 점심시간에는 웨이팅 좀 있어요.',
      latitude: '37.12345',
      longitude: '-122.54321',
    },
  ],
};

const SelectedTopic = () => {
  const { topicId } = useParams();
  const navigator = useNavigate();
  const [topicDetail, setTopicDetail] = useState<TopicInfoType | null>(null);
  const [selectedPinId, setSelectedPinId] = useState<string | null>(null);

  const onClickSetSelectedPinId = (pinId: string) => {
    setSelectedPinId(pinId);

    navigator(`/topics/${topicId}?pinDetail=${pinId}`);
  };

  useEffect(() => {
    // TODO : GET topics/{topicId} & 상태 결합

    setTopicDetail(topicData);

    const queryParams = new URLSearchParams(location.search);
    if (queryParams.has('pinDetail')) {
      setSelectedPinId(queryParams.get('pinDetail'));
    }
  }, []);

  if (!topicDetail) return <></>;

  return (
    <>
      <Space size={3} />
      <Flex $flexDirection="column">
        <TopicInfo
          topicParticipant={12}
          pinNumber={topicDetail.pinCount}
          topicTitle={topicDetail.name}
          topicOwner={'하지원'}
          topicDescription={topicDetail.description}
        />
        <Space size={3} />
        <div>
          {topicDetail.pins.map((pin) => (
            <div
              key={pin.id}
              onClick={() => {
                onClickSetSelectedPinId(pin.id);
              }}
            >
              <PinPreview
                pinTitle={pin.name}
                pinLocation={pin.address}
                pinInformation={pin.description}
              />
              <Space size={3} />
            </div>
          ))}
        </div>

        {selectedPinId && (
          <Flex
            $backgroundColor="white"
            width="400px"
            $minHeight="100vh"
            position="absolute"
            left="400px"
            top="0px"
            padding={4}
            $flexDirection="column"
            $borderLeft={`1px solid ${theme.color.gray}`}
          >
            <PinDetail pinId={selectedPinId} />
          </Flex>
        )}

        <Space size={4} />
      </Flex>
    </>
  );
};

export default SelectedTopic;
