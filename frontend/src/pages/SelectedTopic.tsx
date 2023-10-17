import {
  Fragment,
  lazy,
  Suspense,
  useContext,
  useEffect,
  useState,
} from 'react';
import { useParams, useSearchParams } from 'react-router-dom';
import { styled } from 'styled-components';

import { getApi } from '../apis/getApi';
import SeeTogetherNotFilledSVG from '../assets/seeTogetherBtn_notFilled.svg';
import Button from '../components/common/Button';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Text from '../components/common/Text';
import PullPin from '../components/PullPin';
import PinsOfTopicSkeleton from '../components/Skeletons/PinsOfTopicSkeleton';
import { LAYOUT_PADDING, SIDEBAR } from '../constants';
import { CoordinatesContext } from '../context/CoordinatesContext';
import { SeeTogetherContext } from '../context/SeeTogetherContext';
import { TagContext } from '../context/TagContext';
import useNavigator from '../hooks/useNavigator';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import useMapStore from '../store/mapInstance';
import { PinProps } from '../types/Pin';
import { TopicDetailProps } from '../types/Topic';
import PinDetail from './PinDetail';

const PinsOfTopic = lazy(() => import('../components/PinsOfTopic'));

const getAvailableWidth = (sidebarWidth: number = 372) =>
  window.innerWidth - sidebarWidth;

const getAvailableHeight = () => window.innerHeight;

function SelectedTopic() {
  const { topicId } = useParams();
  const [searchParams, _] = useSearchParams();
  const [topicDetails, setTopicDetails] = useState<TopicDetailProps[] | null>(
    null,
  );
  const [selectedPinId, setSelectedPinId] = useState<number | null>(null);
  const [isOpen, setIsOpen] = useState(true);
  const [isEditPinDetail, setIsEditPinDetail] = useState<boolean>(false);
  const { routePage } = useNavigator();
  const { setCoordinates } = useContext(CoordinatesContext);
  const { tags, setTags } = useContext(TagContext);
  const { width } = useSetLayoutWidth(SIDEBAR);
  const { navbarHighlights: __ } = useSetNavbarHighlight(
    topicId && topicId.split(',').length > 1 ? 'seeTogether' : 'home',
  );
  const { seeTogetherTopics, setSeeTogetherTopics } =
    useContext(SeeTogetherContext);
  const { mapInstance } = useMapStore((state) => state);

  const resizeMap = () => {
    if (!mapInstance) return;

    mapInstance.resize(getAvailableWidth(372), getAvailableHeight());
  };

  const goToHome = () => {
    routePage('/');
  };

  const getAndSetDataFromServer = async () => {
    if (topicId === '-1') return;

    const data = await getApi<TopicDetailProps[]>(`/topics/ids?ids=${topicId}`);

    const topicHashmap = new Map([]);

    setTopicDetails(data);

    // 각 topic의 pin들의 좌표를 가져옴
    const newCoordinates: any = [];

    data.forEach((topic: TopicDetailProps) => {
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

    data.forEach((topicDetailFromData: TopicDetailProps) =>
      topicHashmap.set(`${topicDetailFromData.id}`, topicDetailFromData),
    );

    const topicDetailFromData = topicId
      ?.split(',')
      .map((number) => topicHashmap.get(number)) as TopicDetailProps[];

    if (!topicDetailFromData) return;

    setTopicDetails([...topicDetailFromData]);
  };

  const onClickConfirm = () => {
    routePage('/new-topic', tags.map((tag) => tag.id).join(','));
  };

  const onTagCancel = () => {
    setTags([]);
  };

  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);
    if (queryParams.has('pinDetail')) {
      setSelectedPinId(Number(queryParams.get('pinDetail')));
    } else {
      setSelectedPinId(null);
    }

    setIsOpen(true);
  }, [searchParams]);

  useEffect(() => {
    getAndSetDataFromServer();
    setTags([]);
    if (window.innerWidth > 1180) resizeMap();
  }, []);

  const togglePinDetail = () => {
    setIsOpen(!isOpen);
  };

  if (!seeTogetherTopics) return <></>;

  if (seeTogetherTopics.length === 0 && topicId === '-1') {
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

  if (!topicDetails) return <></>;
  if (!topicId) return <></>;

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
          onClickConfirm={onClickConfirm}
          onClickClose={onTagCancel}
        />
      )}
      <Suspense fallback={<PinsOfTopicSkeleton />}>
        {topicDetails.map((topicDetail, idx) => (
          <Fragment key={topicDetail.id}>
            <PinsOfTopic
              topicId={topicId.split(',')[idx]}
              topicDetail={topicDetail}
              setSelectedPinId={setSelectedPinId}
              setIsEditPinDetail={setIsEditPinDetail}
              setTopicsFromServer={getAndSetDataFromServer}
            />
            {idx !== topicDetails.length - 1 ? <Space size={9} /> : <></>}
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
    top:45%;
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

const WrapperWhenEmpty = styled.section<{ width: '372px' | '100vw' }>`
  width: ${({ width }) => `calc(${width} - 40px)`};
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  margin: 0 auto;
`;

export default SelectedTopic;
