import { DEFAULT_TOPIC_IMAGE } from '../../constants';
import { TagProps } from '../../types/Tag';
import { TopicInfoType } from '../../types/Topic';
import Space from '../common/Space';
import PinPreview from '../PinPreview';
import TopicInfo from '../TopicInfo';

interface PinsOfTopicProps {
  topicId: number;
  tags: TagProps[];
  topicDetail: TopicInfoType[];
  setSelectedPinId: React.Dispatch<React.SetStateAction<number | null>>;
  setTags: React.Dispatch<React.SetStateAction<TagProps[]>>;
  setIsEditPinDetail: React.Dispatch<React.SetStateAction<boolean>>;
}

const PinsOfTopic = ({
  topicId,
  tags,
  topicDetail,
  setSelectedPinId,
  setTags,
  setIsEditPinDetail,
}: PinsOfTopicProps) => {
  return (
    <>
      {topicDetail.map((topic, idx) => {
        return (
          <ul key={topic.id}>
            {idx !== 0 && <Space size={5} />}
            <TopicInfo
              fullUrl={String(topicId)}
              topicId={Number(String(topicId).split(',')[idx])}
              topicImage={DEFAULT_TOPIC_IMAGE}
              topicParticipant={1}
              topicPinCount={topic.pinCount}
              topicTitle={topic.name}
              topicOwner={'토픽을 만든 사람'}
              topicDescription={topic.description}
            />
            {topic.pins.map((pin, idx) => (
              <li key={pin.id}>
                <PinPreview
                  idx={idx}
                  pinTitle={pin.name}
                  pinLocation={pin.address}
                  pinInformation={pin.description}
                  setSelectedPinId={setSelectedPinId}
                  pinId={Number(pin.id)}
                  topicId={topicId}
                  tags={tags}
                  setTags={setTags}
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
