import { DEFAULT_TOPIC_IMAGE } from '../../constants';
import { TagProps } from '../../types/Tag';
import { TopicInfoType } from '../../types/Topic';
import Space from '../common/Space';
import PinPreview from '../PinPreview';
import TopicInfo from '../TopicInfo';

interface PinsOfTopicProps {
  topicId: string;
  topicDetail: TopicInfoType;
  tags: TagProps[];
  setSelectedPinId: React.Dispatch<React.SetStateAction<number | null>>;
  setTags: React.Dispatch<React.SetStateAction<TagProps[]>>;
  setIsEditPinDetail: React.Dispatch<React.SetStateAction<boolean>>;
}

const PinsOfTopic = ({
  topicId,
  topicDetail,
  tags,
  setSelectedPinId,
  setTags,
  setIsEditPinDetail,
}: PinsOfTopicProps) => {
  return (
    <>
      <ul>
        <TopicInfo
          fullUrl={String(topicId)}
          topicId={topicId}
          topicImage={DEFAULT_TOPIC_IMAGE}
          topicParticipant={1}
          topicPinCount={topicDetail.pinCount}
          topicTitle={topicDetail.name}
          topicOwner={'토픽을 만든 사람'}
          topicDescription={topicDetail.description}
        />
        {topicDetail.pins.map((pin, idx) => (
          <li key={pin.id}>
            <PinPreview
              idx={idx}
              pinTitle={pin.name}
              pinLocation={pin.address}
              pinInformation={pin.description}
              setSelectedPinId={setSelectedPinId}
              pinId={Number(pin.id)}
              topicId={String(topicId)}
              tags={tags}
              setTags={setTags}
              setIsEditPinDetail={setIsEditPinDetail}
            />
          </li>
        ))}
      </ul>
    </>
  );
};

export default PinsOfTopic;
