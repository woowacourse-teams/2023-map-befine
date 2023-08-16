import { styled } from 'styled-components';
import Flex from '../common/Flex';
import Space from '../common/Space';
import Text from '../common/Text';
import useNavigator from '../../hooks/useNavigator';
import { useEffect, useRef, useState, KeyboardEvent } from 'react';
import theme from '../../themes';
import Box from '../common/Box';

export interface PinPreviewProps {
  idx: number;
  pinTitle: string;
  pinLocation: string;
  pinInformation: string;
  setSelectedPinId: React.Dispatch<React.SetStateAction<number | null>>;
  pinId: number;
  topicId: string | undefined;
  tagPins: string[];
  setTagPins: React.Dispatch<React.SetStateAction<string[]>>;
  taggedPinIds: number[];
  setTaggedPinIds: React.Dispatch<React.SetStateAction<number[]>>;
  setIsEditPinDetail: React.Dispatch<React.SetStateAction<boolean>>;
}

const PinPreview = ({
  idx,
  pinTitle,
  pinLocation,
  pinInformation,
  setSelectedPinId,
  pinId,
  topicId,
  tagPins,
  setTagPins,
  taggedPinIds,
  setTaggedPinIds,
  setIsEditPinDetail,
}: PinPreviewProps) => {
  const { routePage } = useNavigator();

  const [announceText, setAnnounceText] = useState<string>('토픽 핀 선택');

  const inputRef = useRef<HTMLInputElement | null>(null);

  const onAddTagOfTopic = (e: React.ChangeEvent<HTMLInputElement>) => {
    e.stopPropagation();

    if (e.target.checked) {
      setTagPins([...tagPins, pinTitle]);
      setTaggedPinIds((prev) => [...prev, pinId]);

      if (taggedPinIds.length === 0) {
        setAnnounceText(`핀 ${pinTitle}이 태그에 추가됨. 뽑아오기 기능 활성화`);
        return;
      }
      setAnnounceText(`핀 ${pinTitle}이 태그에 추가됨`);
    } else {
      setTagPins(tagPins.filter((value) => value !== pinTitle));
      setTaggedPinIds(taggedPinIds.filter((value) => value !== pinId));

      if (taggedPinIds.length === 1) {
        setAnnounceText(
          `핀 ${pinTitle}이 태그에서 삭제됨. 뽑아오기 기능 비활성화`,
        );
        return;
      }
      setAnnounceText(`핀 ${pinTitle}이 태그에서 삭제됨`);
    }
  };

  const onClickSetSelectedPinId = () => {
    setSelectedPinId(pinId);
    setIsEditPinDetail(false);
    routePage(`/topics/${topicId}?pinDetail=${pinId}`);
  };

  const onInputKeyDown = (e: KeyboardEvent<HTMLInputElement>) => {
    if (e.keyCode === 13) {
      e.preventDefault();
      inputRef.current?.click();
    }
  };

  useEffect(() => {
    if (announceText) {
      const liveRegion = document.getElementById('live-region');
      if (liveRegion) {
        liveRegion.innerText = announceText;
      }
    }
  }, [announceText]);

  return (
    <Flex
      height="150px"
      position="relative"
      $justifyContent="space-between"
      $backgroundColor="white"
      $borderTop={idx === 0 ? `1px solid ${theme.color.lightGray}` : ''}
      $borderBottom={`1px solid ${theme.color.lightGray}`}
    >
      <Flex
        $flexDirection="column"
        cursor="pointer"
        onClick={onClickSetSelectedPinId}
        width="88%"
        height="95%"
        tabIndex={0}
        role="button"
      >
        <Space size={1} />
        <EllipsisTitle color="black" $fontSize="default" $fontWeight="bold">
          {pinTitle}
        </EllipsisTitle>
        <Space size={0} />
        <Text color="gray" $fontSize="small" $fontWeight="normal">
          {pinLocation}
        </Text>
        <Space size={3} />
        <EllipsisDescription
          color="black"
          $fontSize="small"
          $fontWeight="normal"
        >
          {pinInformation}
        </EllipsisDescription>
      </Flex>
      <Box>
        <Space size={0} />
        <MultiSelectButton
          type="checkbox"
          onChange={onAddTagOfTopic}
          onKeyDown={onInputKeyDown}
          checked={Boolean(taggedPinIds.includes(pinId))}
          aria-label={`${pinTitle} 핀 선택`}
          ref={inputRef}
        />
      </Box>
      <div
        id="live-region"
        aria-live="assertive"
        style={{ position: 'absolute', left: '-9999px' }}
      ></div>
    </Flex>
  );
};

const MultiSelectButton = styled.input`
  width: 24px;
  height: 24px;
  cursor: pointer;
`;

const EllipsisTitle = styled(Text)`
  width: 100%;
  display: -webkit-box;
  word-wrap: break-word;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  text-overflow: ellipsis;
  overflow: hidden;
`;

const EllipsisDescription = styled(Text)`
  width: 100%;
  display: -webkit-box;
  word-wrap: break-word;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  text-overflow: ellipsis;
  overflow: hidden;
`;

export default PinPreview;
