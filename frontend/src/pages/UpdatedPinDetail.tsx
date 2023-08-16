import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Text from '../components/common/Text';
import Box from '../components/common/Box';
import { putApi } from '../apis/putApi';
import { SetURLSearchParams } from 'react-router-dom';
import { ModifyPinFormProps } from '../types/FormValues';
import InputContainer from '../components/InputContainer';
import { hasErrorMessage, hasNullValue } from '../validations';
import useToast from '../hooks/useToast';

interface UpdatedPinDetailProps {
  searchParams: URLSearchParams;
  pinId: number;
  formValues: ModifyPinFormProps;
  errorMessages: Record<keyof ModifyPinFormProps, string>;
  setSearchParams: SetURLSearchParams;
  setIsEditing: React.Dispatch<React.SetStateAction<boolean>>;
  onChangeInput: (
    e: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>,
    isRequired: boolean,
    maxLength?: number | undefined,
  ) => void;
}

const UpdatedPinDetail = ({
  searchParams,
  pinId,
  formValues,
  errorMessages,
  setSearchParams,
  setIsEditing,
  onChangeInput,
}: UpdatedPinDetailProps) => {
  const { showToast } = useToast();

  const removeQueryString = (key: string) => {
    const updatedSearchParams = { ...Object.fromEntries(searchParams) };
    delete updatedSearchParams[key];
    setSearchParams(updatedSearchParams);
  };

  const onClickUpdatePin = async () => {
    if (hasErrorMessage(errorMessages) || hasNullValue(formValues, 'images')) {
      showToast('error', '입력하신 항목들을 다시 한 번 확인해주세요.');
      return;
    }
    try {
      await putApi(`/pins/${pinId}`, formValues);
      setIsEditing(false);
      removeQueryString('edit');
    } catch (error) {
      showToast('error', '해당 지도에 대해 수정 권한이 없습니다. ');
    }
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
        <Space size={1} />
        <Text
          color="white"
          $fontSize="default"
          $fontWeight="normal"
          $textAlign="center"
        >
          + 사진을 추가해주시면 더 알찬 정보를 제공해줄 수 있을 것 같아요.
        </Text>
      </Flex>

      <Space size={5} />

      <InputContainer
        tagType="input"
        containerTitle="장소 이름"
        isRequired={false}
        name="name"
        value={formValues.name}
        placeholder="지도를 클릭하거나 장소의 이름을 입력해주세요."
        onChangeInput={onChangeInput}
        tabIndex={1}
        errorMessage={errorMessages.name}
        autoFocus
        maxLength={50}
      />

      <Space size={5} />

      <InputContainer
        tagType="textarea"
        containerTitle="장소 설명"
        isRequired={false}
        name="description"
        value={formValues.description}
        placeholder="1000자 이내로 장소에 대한 의견을 남겨주세요."
        onChangeInput={onChangeInput}
        tabIndex={2}
        errorMessage={errorMessages.description}
        autoFocus
        maxLength={1000}
      />

      <Space size={3} />

      <Flex $justifyContent="end">
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
        <Space size={2} />
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
      </Flex>
    </Flex>
  );
};

export default UpdatedPinDetail;
