import { useEffect, useState } from 'react';
import Space from '../components/common/Space';
import Flex from '../components/common/Flex';
import PinPreview from '../components/PinPreview';
import TopicInfo from '../components/TopicInfo';
import { TopicInfoType } from '../types/Topic';
import { useParams, useSearchParams } from 'react-router-dom';
import theme from '../themes';
import PinDetail from './PinDetail';
import { getApi } from '../utils/getApi';
import useNavigator from '../hooks/useNavigator';

const SelectedTopic = () => {
  const { topicId } = useParams();
  const { routePage } = useNavigator();
  const [searchParams, setSearchParams] = useSearchParams();
  const [topicDetail, setTopicDetail] = useState<TopicInfoType | null>(null);
  const [selectedPinId, setSelectedPinId] = useState<string | null>(null);

  const onClickSetSelectedPinId = (pinId: string) => {
    setSelectedPinId(pinId);

    routePage(`/topics/${topicId}?pinDetail=${pinId}`);
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

  if (!topicDetail) return <></>;

  return (
    <>
      <Flex $flexDirection="column">
        <ul>
          <TopicInfo
            topicParticipant={12}
            pinNumber={topicDetail.pinCount}
            topicTitle={topicDetail.name}
            topicOwner={'하지원'}
            topicDescription={topicDetail.description}
          />
          {topicDetail.pins.map((pin) => (
            <li
              key={pin.id}
              onClick={() => {
                onClickSetSelectedPinId(pin.id);
              }}
            >
              <Space size={3} />
              <PinPreview
                pinTitle={pin.name}
                pinLocation={pin.address}
                pinInformation={pin.description}
              />
            </li>
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
      </Flex>
    </>
  );
};

export default SelectedTopic;
