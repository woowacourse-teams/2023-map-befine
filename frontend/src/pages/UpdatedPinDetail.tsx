import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Text from '../components/common/Text';
import { putApi } from '../apis/putApi';
import { SetURLSearchParams } from 'react-router-dom';
import { ModifyPinFormProps } from '../types/FormValues';
import InputContainer from '../components/InputContainer';
import { hasErrorMessage, hasNullValue } from '../validations';
import useToast from '../hooks/useToast';
import Button from '../components/common/Button';
import styled from 'styled-components';

interface UpdatedPinDetailProps {
  searchParams: URLSearchParams;
  pinId: number;
  formValues: ModifyPinFormProps;
  errorMessages: Record<keyof ModifyPinFormProps, string>;
  setSearchParams: SetURLSearchParams;
  updatePinDetailAfterEditing: () => void;
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
  updatePinDetailAfterEditing,
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
      updatePinDetailAfterEditing();

      showToast('info', '핀 수정을 완료하였습니다.');
    } catch (error) {
      showToast('error', '해당 지도에 대해 수정 권한이 없습니다. ');
    }
  };

  const onClickCancelPinUpdate = () => {
    setIsEditing(false);
    removeQueryString('edit');
  };

  return (
    <Wrapper>
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
        isRequired={true}
        name="name"
        value={formValues.name}
        placeholder="50글자 이내로 장소의 이름을 입력해주세요."
        onChangeInput={onChangeInput}
        tabIndex={1}
        errorMessage={errorMessages.name}
        autoFocus
        maxLength={50}
      />

      <Space size={1} />

      <InputContainer
        tagType="textarea"
        containerTitle="장소 설명"
        isRequired={true}
        name="description"
        value={formValues.description}
        placeholder="1000자 이내로 장소에 대한 의견을 남겨주세요."
        onChangeInput={onChangeInput}
        tabIndex={2}
        errorMessage={errorMessages.description}
        autoFocus
        maxLength={1000}
      />

      <Space size={6} />

      <Flex $justifyContent="end">
        <Button variant="secondary" onClick={onClickCancelPinUpdate}>
          취소하기
        </Button>
        <Space size={3} />
        <Button variant="primary" onClick={onClickUpdatePin}>
          수정하기
        </Button>
      </Flex>

      <Space size={8} />
    </Wrapper>
  );
};

const Wrapper = styled.div`
  margin: 0 auto;

  @media (max-width: 1076px) {
    width: calc(50vw - 40px);
  }

  @media (max-width: 744px) {
    width: 332px;
    margin: 0 auto;
  }
`;

export default UpdatedPinDetail;
