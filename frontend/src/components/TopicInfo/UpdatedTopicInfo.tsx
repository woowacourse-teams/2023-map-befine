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
import usePost from '../../apiHooks/usePost';
import useDelete from '../../apiHooks/useDelete';
import usePut from '../../apiHooks/usePut';
import useToast from '../../hooks/useToast';

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
  const { fetchPost } = usePost();
  const { fetchGet } = useGet();
  const { fetchDelete } = useDelete();
  const { fetchPut } = usePut();
  const { showToast } = useToast();
  const { formValues, errorMessages, onChangeInput } =
    useFormValues<FormValues>({
      name,
      description,
    });

  const [topicAuthorInfo, setTopicAuthorInfo] =
    useState<TopicAuthorInfo | null>(null);
  const [isPrivate, setIsPrivate] = useState(false); // 혼자 볼 지도 :  같이 볼 지도
  const [isPublic, setIsPublic] = useState(true); // 모두 : 지정 인원
  const [authorizedMemberIds, setAuthorizedMemberIds] = useState<number[]>([]);

  const updateTopicInfo = async () => {
    try {
      await fetchPut({
        url: `/topics/${id}`,
        payload: {
          name: formValues.name,
          image,
          description: formValues.description,
          publicity: isPrivate ? 'PRIVATE' : 'PUBLIC',
          permissionType: isPublic && !isPrivate ? 'ALL_MEMBERS' : 'GROUP_ONLY',
        },
        errorMessage: `권한은 '공개 ➡️ 비공개', '모두 ➡️ 친구', '친구 ➡️ 혼자' 로 변경할 수 없습니다.`,
        isThrow: true,
      });

      if (authorizedMemberIds.length > 0) await updateTopicAuthority();

      showToast('info', '지도를 수정하였습니다.');
      setIsUpdate(false);
    } catch {}
  };

  const updateTopicAuthority = async () => {
    // topicAuthorInfo api 구조 이상으로 권한 설정 자체에 대한 id를 사용 (topicId 아님)
    await fetchDelete({
      url: `/permissions/${topicAuthorInfo?.permissionedMembers[0].id}`,
      errorMessage: '권한 삭제에 실패했습니다.',
      isThrow: true,
    });

    await fetchPost({
      url: '/permissions',
      payload: {
        topicId: id,
        memberIds: authorizedMemberIds,
      },
      errorMessage: '권한 설정에 실패했습니다.',
      isThrow: true,
    });
  };

  const cancelUpdateTopicInfo = () => {
    setIsUpdate(false);
  };

  useEffect(() => {
    fetchGet<TopicAuthorInfo>(
      `/permissions/topics/${id}`,
      '지도 권한 설정 정보를 가져오는데 실패했습니다.',
      (response) => {
        setTopicAuthorInfo(response);
        setIsPrivate(response.publicity === 'PRIVATE');

        setIsPublic(response.permissionedMembers.length === 0);
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
        isPublic={isPublic}
        authorizedMemberIds={authorizedMemberIds}
        setIsPrivate={setIsPrivate}
        setIsPublic={setIsPublic}
        setAuthorizedMemberIds={setAuthorizedMemberIds}
        permissionedMembers={topicAuthorInfo?.permissionedMembers}
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
        <Button tabIndex={7} variant="primary" onClick={updateTopicInfo}>
          수정하기
        </Button>
      </Flex>

      <Space size={6} />
    </Wrapper>
  );
};

const Wrapper = styled.section``;

export default UpdatedTopicInfo;
