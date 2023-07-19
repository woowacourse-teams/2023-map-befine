import Space from '../components/common/Space';
import Flex from '../components/common/Flex';
import PinPreview from '../components/PinPreview';
import TopicInfo from '../components/TopicInfo';
import { Fragment, useEffect, useState } from 'react';
import { getApi } from '../utils/getApi';
import { useLocation } from 'react-router-dom';


interface PinProps {
  id: string;
  name: string;
  address: string;
  description: string;
  latitude: string;
  longtitude: string;
}

export interface TopicPinProps {
  id: string;
  name: string;
  description: string;
  pinCount: number;
  updatedAt: string;
  pins: PinProps[];
}

const SelectedTopic = () => {
  const [topic, setTopic] = useState<TopicPinProps>();

  const { state } = useLocation();

  const getAndSetDataFromServer = async () => {
    const data = await getApi(`/topics/${state}`);
    setTopic(data);
  };

  useEffect(() => {
    getAndSetDataFromServer();
  }, []);

  return (
    <Flex $flexDirection="column">
      {topic && (
        <Fragment>
          <TopicInfo
          topicId={topic.id}
            pinNumber={topic.pinCount}
            topicTitle={topic.name}
            topicDescription={topic.description}
          />
          <Space size={3} />

          {topic.pins &&
            topic.pins.map((info) => {
              return (
                <Fragment key={info.id}>
                  <PinPreview
                    pinTitle={info.name}
                    pinLocation={info.address}
                    pinInformation={info.description}
                  />
                  <Space size={3} />
                </Fragment>
              );
            })}
        </Fragment>
      )}

      <Space size={4} />
    </Flex>
  );
};

export default SelectedTopic;
