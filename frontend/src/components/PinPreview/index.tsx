import { styled } from 'styled-components';
import Flex from '../common/Flex';
import Space from '../common/Space';
import Text from '../common/Text';
import useNavigator from '../../hooks/useNavigator';
import { useEffect, useRef, useState, KeyboardEvent } from 'react';

export interface PinPreviewProps {
  pinTitle: string;
  pinLocation: string;
  pinInformation: string;
  setSelectedPinId: (value: number) => void;
  pinId: number;
  topicId: string | undefined;
  tagPins: string[];
  setTagPins: (value: string[]) => void;
  taggedPinIds: number[];
  setTaggedPinIds: React.Dispatch<React.SetStateAction<number[]>>;
}

const PinPreview = ({
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
}: PinPreviewProps) => {
  const { routePage } = useNavigator();

  const [announceText, setAnnounceText] = useState<string>('토픽 핀 선택');

  const inputRef = useRef<HTMLInputElement | null>(null);

  const onAddTagOfTopic = (e: React.ChangeEvent<HTMLInputElement>) => {
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
      width="360px"
      height="150px"
      position="relative"
      $flexDirection="column"
      $backgroundColor="white"
      $borderBottom="1px solid #E7E7E7"
    >
      <MultiSelectButton
        type="checkbox"
        onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
          onAddTagOfTopic(e)
        }
        onKeyDown={onInputKeyDown}
        checked={Boolean(taggedPinIds.includes(pinId))}
        aria-label={`${pinTitle} 핀 선택`}
        ref={inputRef}
      />
      <Flex
        $flexDirection="column"
        cursor="pointer"
        onClick={onClickSetSelectedPinId}
        width="90%"
        height="95%"
        tabIndex={0}
        role="button"
      >
        <Text color="black" $fontSize="default" $fontWeight="bold">
          {pinTitle}
        </Text>
        <Space size={0} />
        <Text color="gray" $fontSize="small" $fontWeight="normal">
          {pinLocation}
        </Text>
        <Space size={3} />
        <EllipsisText color="black" $fontSize="small" $fontWeight="normal">
          {pinInformation}
        </EllipsisText>
      </Flex>
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
  position: absolute;
  top: 4px;
  right: 4px;
  background-color: ${({ theme }) => theme.color.white};
  border: 1px solid ${({ theme }) => theme.color.black};
  border-radius: ${({ theme }) => theme.radius.small};
  cursor: pointer;

  &:focus {
    background-color: ${({ theme }) => theme.color.primary};
  }
`;

const EllipsisText = styled(Text)`
  width: 100%;
  display: -webkit-box;
  word-wrap: break-word;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  text-overflow: ellipsis;
  overflow: hidden;
`;

export default PinPreview;
