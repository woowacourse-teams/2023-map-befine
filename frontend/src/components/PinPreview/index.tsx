import { styled } from 'styled-components';
import Flex from '../common/Flex';
import Space from '../common/Space';
import Text from '../common/Text';
import useNavigator from '../../hooks/useNavigator';
import { useContext } from 'react';
import { TagIdContext } from '../../store/TagId';

export interface PinPreviewProps {
  pinTitle: string;
  pinLocation: string;
  pinInformation: string;
  setSelectedPinId: (value: number) => void;
  pinId: number;
  topicId: number | undefined;
  tagPins: string[];
  setTagPins: (value: string[]) => void;
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
}: PinPreviewProps) => {
  const { routePage } = useNavigator();

  const { tagId, setTagId } = useContext(TagIdContext) ?? {
    tagId: [],
    setTagId: () => {},
  };

  const onAddTagOfTopic = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.checked) {
      setTagPins([...tagPins, pinTitle]);
      setTagId([...tagId, pinId]);
    } else {
      setTagPins(tagPins.filter((value) => value !== pinTitle));
      setTagId(tagId.filter((value) => value !== pinId));
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
      cursor="pointer"
    >
      <MultiSelectButton
        type="checkbox"
        onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
          onAddTagOfTopic(e)
        }
        checked={Boolean(tagId.includes(pinId))}
      />
      <Flex
        $flexDirection="column"
        cursor="pointer"
        onClick={onClickSetSelectedPinId}
      >
        <Text color="black" $fontSize="default" $fontWeight="bold">
          {pinTitle}
        </Text>
        <Text color="gray" $fontSize="small" $fontWeight="normal">
          {pinLocation}
        </Text>
        <Space size={3} />
        <Text color="black" $fontSize="small" $fontWeight="normal">
          {pinInformation}
        </Text>
      </Flex>
    </Flex>
  );
};

const PinButton = styled.button`
  width: 16px;
  height: 16px;

  position: absolute;
  top: 8px;
  right: 8px;

  background-color: ${({ theme }) => theme.color.white};
  border: 1px solid ${({ theme }) => theme.color.black};
  border-radius: ${({ theme }) => theme.radius.medium};

  &:focus {
    background-color: ${({ theme }) => theme.color.primary};
  }
`;

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

export default PinPreview;
