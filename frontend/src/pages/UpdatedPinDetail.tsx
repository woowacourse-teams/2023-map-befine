import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Text from '../components/common/Text';
import Plus from '../assets/plus.svg';
import Box from '../components/common/Box';
import Textarea from '../components/common/Textarea';
import Input from '../components/common/Input';
import { putApi } from '../utils/putApi';
import { SetURLSearchParams } from 'react-router-dom';
import { ModifyPinFormValuesType } from '../types/FormValues';

interface UpdatedPinDetailProps {
  searchParams: URLSearchParams;
  pinId: number;
  formValues: ModifyPinFormValuesType;
  setSearchParams: SetURLSearchParams;
  setIsEditing: React.Dispatch<React.SetStateAction<boolean>>;
  onChangeInput: (
    e: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>,
  ) => void;
}

const UpdatedPinDetail = ({
  searchParams,
  pinId,
  formValues,
  setSearchParams,
  setIsEditing,
  onChangeInput,
}: UpdatedPinDetailProps) => {
  const removeQueryString = (key: string) => {
    const updatedSearchParams = { ...Object.fromEntries(searchParams) };
    delete updatedSearchParams[key];
    setSearchParams(updatedSearchParams);
  };

  const onClickUpdatePin = async () => {
    await putApi(`/pins/${pinId}`, formValues);
    setIsEditing(false);
    removeQueryString('edit');
  };

  const onClickCancelPinUpdate = () => {
    setIsEditing(false);
    removeQueryString('edit');
  };

  return (
    <Flex $flexDirection="column">
      <Flex
        width="100%"
        height="180px"
        $backgroundColor="gray"
        $alignItems="center"
        $justifyContent="center"
        $flexDirection="column"
        padding={7}
        $borderRadius="small"
      >
        <Plus />
        <Space size={1} />
        <Text
          color="white"
          $fontSize="default"
          $fontWeight="normal"
          $textAlign="center"
        >
          사진을 추가해주시면 더 알찬 정보를 제공해줄 수 있을 것 같아요.
        </Text>
      </Flex>
      <Space size={5} />
      <section>
        <Flex>
          <Text color="black" $fontSize="default" $fontWeight="normal">
            장소 이름
          </Text>
        </Flex>
        <Space size={0} />
        <Input
          type="text"
          name="name"
          value={formValues.name}
          onChange={onChangeInput}
        />
      </section>

      <Space size={5} />

      <section>
        <Flex>
          <Text color="black" $fontSize="default" $fontWeight="normal">
            장소 설명
          </Text>
        </Flex>
        <Space size={0} />
        <Textarea
          name="description"
          value={formValues.description}
          onChange={onChangeInput}
        />
      </section>

      <Space size={3} />

      <Flex $justifyContent="end">
        <Box cursor="pointer">
          <Text
            color="primary"
            $fontSize="default"
            $fontWeight="normal"
            onClick={onClickUpdatePin}
          >
            저장
          </Text>
        </Box>
        <Space size={2} />
        <Box cursor="pointer">
          <Text
            color="black"
            $fontSize="default"
            $fontWeight="normal"
            onClick={onClickCancelPinUpdate}
          >
            취소
          </Text>
        </Box>
      </Flex>
    </Flex>
  );
};

export default UpdatedPinDetail;
