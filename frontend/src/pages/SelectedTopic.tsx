import {
  Fragment,
  lazy,
  Suspense,
  useContext,
  useEffect,
  useState,
} from 'react';
import { styled } from 'styled-components';
import Space from '../components/common/Space';
import Flex from '../components/common/Flex';
import { TopicDetailProps } from '../types/Topic';
import { useParams, useSearchParams } from 'react-router-dom';
import theme from '../themes';
import PinDetail from './PinDetail';
import { getApi } from '../apis/getApi';
import PullPin from '../components/PullPin';
import { CoordinatesContext } from '../context/CoordinatesContext';
import useNavigator from '../hooks/useNavigator';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import { LAYOUT_PADDING, SIDEBAR } from '../constants';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import PinsOfTopicSkeleton from '../components/Skeletons/PinsOfTopicSkeleton';
import { TagContext } from '../context/TagContext';
import { PinProps } from '../types/Pin';

const PinsOfTopic = lazy(() => import('../components/PinsOfTopic'));

const SelectedTopic = () => {
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
  const { navbarHighlights: __ } = useSetNavbarHighlight('');

  const getAndSetDataFromServer = async () => {
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
    getAndSetDataFromServer();

    const queryParams = new URLSearchParams(location.search);
    if (queryParams.has('pinDetail')) {
      setSelectedPinId(Number(queryParams.get('pinDetail')));
    } else {
      setSelectedPinId(null);
    }

    setIsOpen(true);
  }, [searchParams]);

  useEffect(() => {
    setTags([]);
  }, []);

  const togglePinDetail = () => {
    setIsOpen(!isOpen);
  };

  if (!topicDetails) return <></>;
  if (!topicId) return <></>;

  return (
    <Flex
      width={`calc(${width} - ${LAYOUT_PADDING})`}
      $flexDirection="column"
      as="section"
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
              topicId={topicId}
              idx={idx}
              topicDetail={topicDetail}
              setSelectedPinId={setSelectedPinId}
              setIsEditPinDetail={setIsEditPinDetail}
              setTopicsFromServer={getAndSetDataFromServer}
            />
            {idx !== topicDetails.length - 1 ? <Space size={9} /> : <></>}
          </Fragment>
        ))}
      </Suspense>

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
                topicId={topicId}
                pinId={selectedPinId}
                isEditPinDetail={isEditPinDetail}
                setIsEditPinDetail={setIsEditPinDetail}
              />
            </Flex>
          </PinDetailWrapper>
        </>
      )}
    </Flex>
  );
};

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

export default SelectedTopic;
