import { TopicInfoType } from '../../types/Topic';
import Space from '../common/Space';
import PinPreview from '../PinPreview';
import TopicInfo from '../TopicInfo';

interface PinsOfTopicProps {
  topicId: string | undefined;
  tagPins: string[];
  topicDetail: TopicInfoType[];
  taggedPinIds: number[];
  setSelectedPinId: React.Dispatch<React.SetStateAction<number | null>>;
  setTagPins: React.Dispatch<React.SetStateAction<string[]>>;
  setTaggedPinIds: React.Dispatch<React.SetStateAction<number[]>>;
  setIsEditPinDetail: React.Dispatch<React.SetStateAction<boolean>>;
}

const PinsOfTopic = ({
  topicId,
  tagPins,
  topicDetail,
  taggedPinIds,
  setSelectedPinId,
  setTagPins,
  setTaggedPinIds,
  setIsEditPinDetail,
}: PinsOfTopicProps) => {
  return (
    <>
      {topicDetail.map((topic, idx) => {
        return (
          <ul key={topic.id}>
            {idx !== 0 && <Space size={5} />}
            <TopicInfo
              fullUrl={topicId}
              topicId={topicId?.split(',')[idx]}
              topicParticipant={1}
              pinNumber={topic.pinCount}
              topicTitle={topic.name}
              topicOwner={'토픽을 만든 사람'}
              topicDescription={topic.description}
            />
            {topic.pins.map((pin) => (
              <li key={pin.id}>
                <Space size={3} />
                <PinPreview
                  pinTitle={pin.name}
                  pinLocation={pin.address}
                  pinInformation={pin.description}
                  setSelectedPinId={setSelectedPinId}
                  pinId={Number(pin.id)}
                  topicId={topicId}
                  tagPins={tagPins}
                  setTagPins={setTagPins}
                  taggedPinIds={taggedPinIds}
                  setTaggedPinIds={setTaggedPinIds}
                  setIsEditPinDetail={setIsEditPinDetail}
                />
              </li>
            ))}
          </ul>
        );
      })}
    </>
  );
};

export default PinsOfTopic;
