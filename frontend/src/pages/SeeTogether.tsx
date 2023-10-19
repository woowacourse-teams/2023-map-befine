import { Fragment, Suspense, useContext, useEffect, useState } from 'react';
import { useLocation, useParams, useSearchParams } from 'react-router-dom';
import { styled } from 'styled-components';

import { getApi } from '../apis/getApi';
import SeeTogetherNotFilledSVG from '../assets/seeTogetherBtn_notFilled.svg';
import Button from '../components/common/Button';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Text from '../components/common/Text';
import PinsOfTopic from '../components/PinsOfTopic';
import PullPin from '../components/PullPin';
import PinsOfTopicSkeleton from '../components/Skeletons/PinsOfTopicSkeleton';
import { LAYOUT_PADDING, SIDEBAR } from '../constants';
import { CoordinatesContext } from '../context/CoordinatesContext';
import { SeeTogetherContext } from '../context/SeeTogetherContext';
import useNavigator from '../hooks/useNavigator';
import useResizeMap from '../hooks/useResizeMap';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import useTags from '../hooks/useTags';
import { PinProps } from '../types/Pin';
import { TopicDetailProps } from '../types/Topic';
import PinDetail from './PinDetail';

function SeeTogether() {
  const { topicId } = useParams();
  const { routePage } = useNavigator();
  const [searchParams, _] = useSearchParams();
  const location = useLocation();

  const [isOpen, setIsOpen] = useState(true);
  const [isEditPinDetail, setIsEditPinDetail] = useState<boolean>(false);
  const [selectedPinId, setSelectedPinId] = useState<number | null>(null);
  const [topicDetails, setTopicDetails] = useState<TopicDetailProps[] | null>(
    null,
  );

  const { tags, setTags, onClickInitTags, onClickCreateTopicWithTags } =
    useTags();
  const { setCoordinates } = useContext(CoordinatesContext);
  const { width } = useSetLayoutWidth(SIDEBAR);
  const { seeTogetherTopics } = useContext(SeeTogetherContext);
  useSetNavbarHighlight('seeTogether');
  useResizeMap();

  const goToHome = () => {
    routePage('/');
  };

  const getAndSetDataFromServer = async () => {
    if (topicId === '-1' || !topicId) return;

    const requestTopicIds = seeTogetherTopics?.join(',');
    const topics = await getApi<TopicDetailProps[]>(
      `/topics/ids?ids=${requestTopicIds}`,
    );

    setCoordinatesTopicDetailWithHashMap(topics);
  };

  const setCoordinatesTopicDetailWithHashMap = (topics: TopicDetailProps[]) => {
    if (topicId === '-1' || !topicId) return;

    const newCoordinates: any = [];
    const topicHashmap = new Map([]);

    topics.forEach((topic: TopicDetailProps) => {
      topic.pins.forEach((pin: PinProps) => {
        newCoordinates.push({
          id: pin.id,
          topicId: topic.id,
          pinName: pin.name,
          latitude: pin.latitude,
          longitude: pin.longitude,
        });
      });
    });

    setCoordinates(newCoordinates);

    topics.forEach((topicDetailFromData: TopicDetailProps) =>
      topicHashmap.set(`${topicDetailFromData.id}`, topicDetailFromData),
    );

    const topicDetailFromData = topicId
      .split(',')
      .map((number) => topicHashmap.get(number)) as TopicDetailProps[];

    setTopicDetails([...topicDetailFromData]);
  };

  const togglePinDetail = () => {
    setIsOpen(!isOpen);
  };

  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);

    if (queryParams.has('pinDetail')) {
      setSelectedPinId(Number(queryParams.get('pinDetail')));
      setIsOpen(true);
      return;
    }

    setSelectedPinId(null);
  }, [searchParams]);

  useEffect(() => {
    getAndSetDataFromServer();
  }, [topicId]);

  useEffect(() => {
    setTags([]);
  }, []);

  if (!seeTogetherTopics || !topicId) return <></>;

  if (seeTogetherTopics.length === 0 || topicId === '-1') {
    return (
      <WrapperWhenEmpty width={width}>
        <Flex $alignItems="center">
          <SeeTogetherNotFilledSVG />
          <Space size={1} />
          <Text color="black" $fontSize="default" $fontWeight="normal">
            버튼을 눌러 지도를 추가해보세요.
          </Text>
          <Space size={4} />
        </Flex>
        <Space size={5} />
        <Button variant="primary" onClick={goToHome}>
          메인페이지로 가기
        </Button>
      </WrapperWhenEmpty>
    );
  }

  return (
    <Wrapper
      width={`calc(${width} - ${LAYOUT_PADDING})`}
      $selectedPinId={selectedPinId}
    >
      <Space size={3} />
      {tags.length > 0 && (
        <PullPin
          tags={tags}
          confirmButton="뽑아오기"
          onClickConfirm={onClickCreateTopicWithTags}
          onClickClose={onClickInitTags}
        />
      )}
      <Suspense fallback={<PinsOfTopicSkeleton />}>
        {topicDetails?.map((topicDetail, idx) => (
          <Fragment key={topicDetail.id}>
            <PinsOfTopic
              urlTopicId={topicDetails
                .map((topicDetail) => topicDetail.id)
                .join(',')}
              topicId={String(topicDetail.id)}
              topicDetail={topicDetail}
              setSelectedPinId={setSelectedPinId}
              setIsEditPinDetail={setIsEditPinDetail}
              setTopicsFromServer={getAndSetDataFromServer}
            />
            {idx !== topicDetails.length - 1 ? <Space size={9} /> : null}
          </Fragment>
        ))}
      </Suspense>

      <Space size={8} />

      {selectedPinId && (
        <>
          <ToggleButton $isCollapsed={!isOpen} onClick={togglePinDetail}>
            ◀
          </ToggleButton>
          <PinDetailWrapper className={isOpen ? '' : 'collapsedPinDetail'}>
            <PinDetail
              width={width}
              pinId={selectedPinId}
              isEditPinDetail={isEditPinDetail}
              setIsEditPinDetail={setIsEditPinDetail}
            />
          </PinDetailWrapper>
        </>
      )}
    </Wrapper>
  );
}

const WrapperWhenEmpty = styled.section<{ width: '372px' | '100vw' }>`
  width: ${({ width }) => `calc(${width} - 40px)`};
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  margin: 0 auto;
`;

const Wrapper = styled.section<{
  width: 'calc(100vw - 40px)' | 'calc(372px - 40px)';
  $selectedPinId: number | null;
}>`
  display: flex;
  flex-direction: column;
  width: ${({ width }) => width};
  margin: 0 auto;

  @media (max-width: 1076px) {
    width: ${({ $selectedPinId }) => ($selectedPinId ? '49vw' : '50vw')};
    margin: ${({ $selectedPinId }) => $selectedPinId && '0'};
  }

  @media (max-width: 744px) {
    width: 100%;
  }
`;

const PinDetailWrapper = styled.div`
  &.collapsedPinDetail {
    z-index: -1;
  }
`;

const ToggleButton = styled.button<{
  $isCollapsed: boolean;
}>`
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
    top: 45%;
    left: 372px;
    z-index: 1;
    `}

  &:hover {
    background-color: #f5f5f5;
  }

  @media (max-width: 1076px) {
    display: none;
  }
`;

export default SeeTogether;