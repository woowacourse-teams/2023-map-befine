import Space from '../components/common/Space';
import Flex from '../components/common/Flex';
import PinPreview from '../components/PinPreview';
import Topic from '../components/Topic';
import { Fragment } from 'react';

const data = [
  {
    topic: {
      topicParticipant: '12명의 참여자',
      pinNumber: '57개의 핀',
      topicTitle: '🍛 선릉 직징안이 추천하는 점심 맛집',
      topicOwner: '하지원',
      topicDescription: '선릉 직장인이 돌아다니면서 기록한 맛집 리스트예요.',
    },
    pin: [
      {
        pinTitle: '잇쇼우',
        pinLocation: '서울특별시 선릉 테헤란로 127길 16',
        pinInformation:
          '돈까스와 모밀, 우동 등 다양한 일식 메뉴가 있어요. 돈까스가 특히 맛있습니다.',
      },
      {
        pinTitle: '오또상스시',
        pinLocation: '서울특별시 선릉 테헤란로 192-46',
        pinInformation:
          '초밥을 파는 곳입니다. 점심 특선 있고 초밥 질이 괜찮습니다. 가격대도 다른 곳에 비해서 양호한 편이고 적당히 생각날...',
      },
      {
        pinTitle: '잇쇼우2',
        pinLocation: '서울특별시 선릉 테헤란로 127길 16',
        pinInformation:
          '돈까스와 모밀, 우동 등 다양한 일식 메뉴가 있어요. 돈까스가 특히 맛있습니다.',
      },
      {
        pinTitle: '오또상스시2',
        pinLocation: '서울특별시 선릉 테헤란로 192-46',
        pinInformation:
          '초밥을 파는 곳입니다. 점심 특선 있고 초밥 질이 괜찮습니다. 가격대도 다른 곳에 비해서 양호한 편이고 적당히 생각날...',
      },
      {
        pinTitle: '잇쇼우3',
        pinLocation: '서울특별시 선릉 테헤란로 127길 16',
        pinInformation:
          '돈까스와 모밀, 우동 등 다양한 일식 메뉴가 있어요. 돈까스가 특히 맛있습니다.',
      },
    ],
  },
];

const Pins = () => {
  return (
    <Flex $flexDirection="column">
      {data &&
        data.map((value) => {
          return (
            <Fragment key={value.topic.topicTitle}>
              <Topic
                topicParticipant={value.topic.topicParticipant}
                pinNumber={value.topic.pinNumber}
                topicTitle={value.topic.topicTitle}
                topicOwner={value.topic.topicOwner}
                topicDescription={value.topic.topicDescription}
              />
              <Space size={3} />

              {value.pin.map((info) => {
                return (
                  <Fragment key={info.pinTitle}>
                    <PinPreview
                      pinTitle={info.pinTitle}
                      pinLocation={info.pinLocation}
                      pinInformation={info.pinInformation}
                    />
                    <Space size={3} />
                  </Fragment>
                );
              })}
            </Fragment>
          );
        })}

      <Space size={4} />
    </Flex>
  );
};

export default Pins;
