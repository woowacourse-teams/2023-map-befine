import { styled } from 'styled-components';
import Flex from '../common/Flex';
import Space from '../common/Space';
import Text from '../common/Text';
import useNavigator from '../../hooks/useNavigator';
import Box from '../common/Box';

export interface PinPreviewProps {
  pinTitle: string;
  pinLocation: string;
  pinInformation: string;
  setSelectedPinId: (value: number) => void;
  pinId: number;
  topicId: number | undefined;
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

  const onAddTagOfTopic = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.checked) {
      setTagPins([...tagPins, pinTitle]);
      setTaggedPinIds((prev) => [...prev, pinId]);
    } else {
      setTagPins(tagPins.filter((value) => value !== pinTitle));
      setTaggedPinIds(taggedPinIds.filter((value) => value !== pinId));
    }
  };

  const onClickSetSelectedPinId = () => {
    setSelectedPinId(pinId);

    routePage(`/topics/${topicId}?pinDetail=${pinId}`);
  };

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
        checked={Boolean(taggedPinIds.includes(pinId))}
      />
      <Flex
        $flexDirection="column"
        cursor="pointer"
        onClick={onClickSetSelectedPinId}
        width="90%"
      >
        <Text color="black" $fontSize="default" $fontWeight="bold">
          {pinTitle}
        </Text>
        <Space size={1} />
        <Text color="gray" $fontSize="small" $fontWeight="normal">
          {pinLocation}
        </Text>
        <Space size={3} />
        <EllipsisText color="black" $fontSize="small" $fontWeight="normal">
          {pinInformation}
        </EllipsisText>
      </Flex>
    </Flex>
  );
};

const MultiSelectButton = styled.input`
  width: 24px;
  height: 24px;
  position: absolute;
  top: 8px;
  right: 8px;
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
