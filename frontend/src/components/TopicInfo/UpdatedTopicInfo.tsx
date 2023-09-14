import styled from 'styled-components';
import InputContainer from '../InputContainer';
import useFormValues from '../../hooks/useFormValues';
import Space from '../common/Space';
import Flex from '../common/Flex';
import Button from '../common/Button';
import { useEffect, useState } from 'react';
import useGet from '../../apiHooks/useGet';
import { TopicAuthorInfo } from '../../types/Topic';
import AuthorityRadioContainer from '../AuthorityRadioContainer';

interface UpdatedTopicInfoProp {
  id: number;
  image: string;
  name: string;
  description: string;
  setIsUpdate: React.Dispatch<React.SetStateAction<boolean>>;
}

interface FormValues {
  name: string;
  description: string;
}

const UpdatedTopicInfo = ({
  id,
  image,
  name,
  description,
  setIsUpdate,
}: UpdatedTopicInfoProp) => {
  const { fetchGet } = useGet();
  const { formValues, errorMessages, onChangeInput } =
    useFormValues<FormValues>({
      name,
      description,
    });

  const [topicAuthorInfo, setTopicAuthorInfo] =
    useState<TopicAuthorInfo | null>(null);
  const [isPrivate, setIsPrivate] = useState(false); // 혼자 볼 지도 :  같이 볼 지도
  const [isAll, setIsAll] = useState(true); // 모두 : 지정 인원
  const [authorizedMemberIds, setAuthorizedMemberIds] = useState<number[]>([]);

  const cancelUpdateTopicInfo = () => {
    setIsUpdate(false);
  };

  useEffect(() => {
    fetchGet<TopicAuthorInfo>(
      `/permissions/topics/${id}`,
      '지도 권한 설정 정보를 가져오는데 실패했습니다.',
      (response) => {
        setTopicAuthorInfo(response);
      },
    );
  }, []);

  return (
    <Wrapper>
      <InputContainer
        tagType="input"
        containerTitle="지도 이름"
        isRequired={true}
        name="name"
        value={formValues.name}
        placeholder="20자 이내로 지도의 이름을 입력해주세요."
        onChangeInput={onChangeInput}
        tabIndex={2}
        errorMessage={errorMessages.name}
        maxLength={20}
      />

      <Space size={1} />

      <InputContainer
        tagType="textarea"
        containerTitle="한 줄 설명"
        isRequired={true}
        name="description"
        value={formValues.description}
        placeholder="100글자 이내로 지도에 대해서 설명해주세요."
        onChangeInput={onChangeInput}
        tabIndex={3}
        errorMessage={errorMessages.description}
        maxLength={100}
      />

      <AuthorityRadioContainer
        isPrivate={isPrivate}
        isAll={isAll}
        authorizedMemberIds={authorizedMemberIds}
        setIsPrivate={setIsPrivate}
        setIsAll={setIsAll}
        setAuthorizedMemberIds={setAuthorizedMemberIds}
        topicAuthorInfo={topicAuthorInfo}
      />

      <Space size={6} />

      <Flex $justifyContent="end">
        <Button
          tabIndex={7}
          type="button"
          variant="secondary"
          onClick={cancelUpdateTopicInfo}
        >
          취소하기
        </Button>
        <Space size={3} />
        <Button tabIndex={7} variant="primary">
          수정하기
        </Button>
      </Flex>

      <Space size={6} />
    </Wrapper>
  );
};

const Wrapper = styled.section``;

export default UpdatedTopicInfo;
