import { useEffect, useState } from 'react';
import styled from 'styled-components';

import useDelete from '../../apiHooks/useDelete';
import useGet from '../../apiHooks/useGet';
import usePost from '../../apiHooks/usePost';
import usePut from '../../apiHooks/usePut';
import useFormValues from '../../hooks/useFormValues';
import useToast from '../../hooks/useToast';
import { TopicAuthorInfo } from '../../types/Topic';
import Button from '../common/Button';
import Flex from '../common/Flex';
import Space from '../common/Space';
import InputContainer from '../InputContainer';
import { postApi } from '../../apis/postApi';
import useCompressImage from '../../hooks/useCompressImage';
import Image from '../common/Image';
import { DEFAULT_TOPIC_IMAGE } from '../../constants';
import { putApi } from '../../apis/putApi';

interface UpdatedTopicInfoProp {
  id: number;
  image: string;
  name: string;
  description: string;
  setIsUpdate: React.Dispatch<React.SetStateAction<boolean>>;
  setTopicsFromServer: () => void;
}

interface FormValues {
  name: string;
  description: string;
}

function UpdatedTopicInfo({
  id,
  image,
  name,
  description,
  setIsUpdate,
  setTopicsFromServer,
}: UpdatedTopicInfoProp) {
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
  const [isAllPermissioned, setIsAllPermissioned] = useState(true); // 모두 : 지정 인원
  const [authorizedMemberIds, setAuthorizedMemberIds] = useState<number[]>([]);
  const { compressImage } = useCompressImage();

  const updateTopicInfo = async () => {
    try {
      await fetchPut({
        url: `/topics/${id}`,
        payload: {
          name: formValues.name,
          image,
          description: formValues.description,
          publicity: isPrivate ? 'PRIVATE' : 'PUBLIC',
          permissionType:
            isAllPermissioned && !isPrivate ? 'ALL_MEMBERS' : 'GROUP_ONLY',
        },
        errorMessage: `권한은 '공개 ➡️ 비공개', '모두 ➡️ 친구', '친구 ➡️ 혼자' 로 변경할 수 없습니다.`,
        isThrow: true,
      });

      // // TODO : 수정해야하는 로직
      // if (authorizedMemberIds.length === 0) {
      //   await deleteTopicPermissionMembers();
      // }

      // if (authorizedMemberIds.length > 0) {
      //   await deleteTopicPermissionMembers();
      //   await updateTopicPermissionMembers();
      // }

      showToast('info', '지도를 수정하였습니다.');
      setIsUpdate(false);
      setTopicsFromServer();
    } catch {}
  };

  const deleteTopicPermissionMembers = async () => {
    if (topicAuthorInfo && topicAuthorInfo.permissionedMembers[0]) {
      await fetchDelete({
        url: `/permissions/${topicAuthorInfo.permissionedMembers[0].id}`,
        errorMessage: '권한 삭제에 실패했습니다.',
        isThrow: true,
      });
    }
  };

  const updateTopicPermissionMembers = async () => {
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

        setIsAllPermissioned(response.permissionedMembers.length === 0);
      },
    );
  }, []);

  const onTopicImageFileChange = async (
    event: React.ChangeEvent<HTMLInputElement>,
  ) => {
    const file = event.target.files && event.target.files[0];
    const formData = new FormData();

    if (!file) {
      showToast(
        'error',
        '이미지를 선택하지 않았거나 추가하신 이미지를 찾을 수 없습니다. 다시 선택해 주세요.',
      );
      return;
    }

    const compressedFile = await compressImage(file);

    formData.append('image', compressedFile);

    await putApi(`/topics/images/${id}`, formData);

    setTopicsFromServer();
  };

  return (
    <Wrapper>
      <ImageWrapper>
        <TopicImage
          height="168px"
          width="100%"
          src={image}
          alt="사진 이미지"
          $objectFit="cover"
          onError={(e: React.SyntheticEvent<HTMLImageElement, Event>) => {
            e.currentTarget.src = DEFAULT_TOPIC_IMAGE;
          }}
        />
        <ImageInputLabel htmlFor="file">수정</ImageInputLabel>
        <ImageInputButton
          id="file"
          type="file"
          name="image"
          onChange={onTopicImageFileChange}
        />
      </ImageWrapper>

      <Space size={5} />

      <InputContainer
        tagType="input"
        containerTitle="지도 이름"
        isRequired
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
        isRequired
        name="description"
        value={formValues.description}
        placeholder="100글자 이내로 지도에 대해서 설명해주세요."
        onChangeInput={onChangeInput}
        tabIndex={3}
        errorMessage={errorMessages.description}
        maxLength={100}
      />

      {/* <AuthorityRadioContainer
        isPrivate={isPrivate}
        isAllPermissioned={isAllPermissioned}
        authorizedMemberIds={authorizedMemberIds}
        setIsPrivate={setIsPrivate}
        setIsAllPermissioned={setIsAllPermissioned}
        setAuthorizedMemberIds={setAuthorizedMemberIds}
        permissionedMembers={topicAuthorInfo?.permissionedMembers}
      /> */}

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
}

const Wrapper = styled.article``;

const ImageWrapper = styled.div`
  position: relative;
`;

const ImageInputLabel = styled.label`
  width: 60px;
  height: 35px;
  margin-bottom: 10px;
  padding: 10px 10px;

  color: ${({ theme }) => theme.color.black};
  background-color: ${({ theme }) => theme.color.lightGray};

  font-size: ${({ theme }) => theme.fontSize.extraSmall};
  font-weight: ${({ theme }) => theme.fontWeight.bold};
  text-align: center;

  border-radius: ${({ theme }) => theme.radius.small};
  cursor: pointer;

  position: absolute;
  left: 40%;
  bottom: -25px;

  box-shadow: rgba(0, 0, 0, 0.3) 0px 0px 5px 0px;

  &:hover {
    filter: brightness(0.95);
  }
`;

const ImageInputButton = styled.input`
  display: none;
`;

const TopicImage = styled(Image)`
  border-radius: ${({ theme }) => theme.radius.medium};
`;

export default UpdatedTopicInfo;
function compressImage(file: File) {
  throw new Error('Function not implemented.');
}