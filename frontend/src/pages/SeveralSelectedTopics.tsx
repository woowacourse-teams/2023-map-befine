import { useContext, useEffect, useState } from 'react';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import { LayoutWidthContext } from '../context/LayoutWidthContext';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import { SIDEBAR } from '../constants';
import { SeeTogetherContext } from '../context/SeeTogetherContext';
import { getApi } from '../apis/getApi';
import { TopicInfoType } from '../types/Topic';
import SeeTogetherSVG from '../assets/seeTogetherBtn_filled.svg';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import { styled } from 'styled-components';
import Text from '../components/common/Text';
import PinsOfTopic from '../components/PinsOfTopic';
import { TagContext } from '../context/TagContext';
import { useSearchParams } from 'react-router-dom';
import PullPin from '../components/PullPin';
import theme from '../themes';
import PinDetail from './PinDetail';
import useNavigator from '../hooks/useNavigator';
import Button from '../components/common/Button';

const SeveralSelectedTopics = () => {
  const [selectedPinId, setSelectedPinId] = useState<number | null>(null);
  const [isEditPinDetail, setIsEditPinDetail] = useState<boolean>(false);
  const [isOpen, setIsOpen] = useState(true);
  const [searchParams, _] = useSearchParams();
  const { routePage } = useNavigator();
  const { navbarHighlights: __ } = useSetNavbarHighlight('seeTogether');
  const { width } = useSetLayoutWidth(SIDEBAR);
  const [topicDetails, setTopicDetails] = useState<TopicInfoType[] | null>([
    {
      id: 1,
      name: '준팍의 두번째 토픽',
      description: '준팍이 막 만든 두번째 토픽',
      // image: 'https://map-befine-official.github.io/favicon.png',
      pinCount: 2,
      updatedAt: '2023-08-14T13:15:52.116056589',
      pins: [
        {
          id: 1,
          name: '매튜의 산스장',
          address: '지번 주소',
          description: '매튜가 사랑하는 산스장',
          latitude: 37.0,
          longitude: 127.0,
          updatedAt: '2023.08.05',
          images: [],
        },
        {
          id: 2,
          name: '매튜의 안갈집',
          address: '지번 주소',
          description: '매튜가 두번은 안 갈 집',
          latitude: 37.0,
          longitude: 127.0,
          updatedAt: '2023.08.05',
          images: [],
        },
      ],
    },
  ]);
  const { tags, setTags } = useContext(TagContext);

  const getSeeTogetherTopics = async () => {
    const topics = await getApi<TopicInfoType[]>('default', '/members/atlas');
    const topicHashmap = new Map([]);

    topics.forEach((topicDetail: TopicInfoType) =>
      topicHashmap.set(`${topicDetail.id}`, topicDetail),
    );

    const topicDetailFromData = topics.map((topic) =>
      topicHashmap.get(topic.id),
    ) as TopicInfoType[];

    if (!topicDetailFromData) return;

    setTopicDetails([...topicDetailFromData]);
  };

  const togglePinDetail = () => {
    setIsOpen(!isOpen);
  };

  const onClickConfirm = () => {
    routePage('/new-topic', tags.map((tag) => tag.id).join(','));
  };

  const onTagCancel = () => {
    setTags([]);
  };

  useEffect(() => {
    // getSeeTogetherTopics();

    const queryParams = new URLSearchParams(location.search);
    if (queryParams.has('pinDetail')) {
      setSelectedPinId(Number(queryParams.get('pinDetail')));
    }

    setIsOpen(true);
  }, [searchParams]);

  if (!topicDetails) {
    return (
      <WrapperWhenEmpty>
        <Flex $alignItems="center">
          <SeeTogetherSVG />
          <Space size={1} />
          <Text color="black" $fontSize="default" $fontWeight="normal">
            버튼을 눌러 지도를 추가해보세요.
          </Text>
        </Flex>
        <Space size={4} />
        <Button
          variant="primary"
          onClick={() => {
            routePage('/');
          }}
        >
          메인페이지로 가기
        </Button>
      </WrapperWhenEmpty>
    );
  }

  return (
    <Wrapper>
      <Space size={2} />
      {tags.length > 0 && (
        <PullPin
          tags={tags}
          confirmButton="뽑아오기"
          onClickConfirm={onClickConfirm}
          onClickClose={onTagCancel}
        />
      )}
      {topicDetails.map((topic, idx) => (
        <>
          <PinsOfTopic
            fromWhere="severalTopics"
            topicId={topic.id}
            tags={tags}
            topicDetail={topic}
            setTags={setTags}
            setSelectedPinId={setSelectedPinId}
            setIsEditPinDetail={setIsEditPinDetail}
          />
          {idx !== topicDetails.length - 1 ? <Space size={9} /> : <></>}
        </>
      ))}

      {selectedPinId && (
        <>
          <ToggleButton $isCollapsed={!isOpen} onClick={togglePinDetail}>
            ◀
          </ToggleButton>
          <PinDetailWrapper className={isOpen ? '' : 'collapsedPinDetail'}>
            <Flex
              $backgroundColor="white"
              width={width}
              height="100vh"
              overflow="auto"
              position="absolute"
              left={width}
              top="0px"
              padding={4}
              $flexDirection="column"
              $borderLeft={`1px solid ${theme.color.gray}`}
              $zIndex={1}
            >
              <PinDetail
                pinId={selectedPinId}
                isEditPinDetail={isEditPinDetail}
                setIsEditPinDetail={setIsEditPinDetail}
              />
            </Flex>
          </PinDetailWrapper>
        </>
      )}
    </Wrapper>
  );
};

const WrapperWhenEmpty = styled.section`
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const Wrapper = styled.section`
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
`;

const PinDetailWrapper = styled.div`
  &.collapsedPinDetail {
    z-index: -1;
  }
`;

const ToggleButton = styled.button<{ $isCollapsed: boolean }>`
  position: absolute;
  top: 50%;
  left: 744px;
  transform: translateY(-50%);
  z-index: 1;
  height: 80px;
  background-color: #fff;
  padding: 12px;
  border-radius: 4px;
  box-shadow: 0px 1px 5px rgba(0, 0, 0, 0.2);
  cursor: pointer;

  ${(props) =>
    props.$isCollapsed &&
    `
    transform: rotate(180deg);
    top:45%;
    left: 372px;
    z-index: 1;
    `}

  &:hover {
    background-color: #f5f5f5;
  }
`;

export default SeveralSelectedTopics;
