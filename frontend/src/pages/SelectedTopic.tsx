import { useEffect, useState } from 'react';
import Space from '../components/common/Space';
import Flex from '../components/common/Flex';
import PinPreview from '../components/PinPreview';
import TopicInfo from '../components/TopicInfo';
import { TopicInfoType } from '../types/Topic';
import { useNavigate, useParams, useSearchParams } from 'react-router-dom';
import theme from '../themes';
import PinDetail from './PinDetail';
import { getApi } from '../utils/getApi';

const SelectedTopic = () => {
  const { topicId } = useParams();
  const [searchParams, setSearchParams] = useSearchParams();
  const navigator = useNavigate();
  const [topicDetail, setTopicDetail] = useState<TopicInfoType | null>(null);
  const [selectedPinId, setSelectedPinId] = useState<string | null>(null);

  const onClickSetSelectedPinId = (pinId: string) => {
    setSelectedPinId(pinId);

    navigator(`/topics/${topicId}?pinDetail=${pinId}`);
  };

  const getAndSetDataFromServer = async () => {
    const data = await getApi(`/topics/${topicId}`);
    setTopicDetail(data);
  };

  useEffect(() => {
    getAndSetDataFromServer();

    const queryParams = new URLSearchParams(location.search);
    if (queryParams.has('pinDetail')) {
      setSelectedPinId(queryParams.get('pinDetail'));
    }
  }, [searchParams]);

  if (!topicId) return <></>;
  if (!topicDetail) return <></>;

  return (
    <>
      <Space size={3} />
      <Flex $flexDirection="column">
        <ul>
          <TopicInfo
            topicParticipant={12}
            pinNumber={topicDetail.pinCount}
            topicTitle={topicDetail.name}
            topicOwner={'하지원'}
            topicDescription={topicDetail.description}
          />
          <Space size={3} />
          {topicDetail.pins.map((pin) => (
            <ul
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
            </ul>
          ))}
        </ul>

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
