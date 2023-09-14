import { useContext, useEffect, useState } from 'react';
import Text from '../components/common/Text';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Button from '../components/common/Button';
import Box from '../components/common/Box';
import useNavigator from '../hooks/useNavigator';
import { NewTopicFormProps } from '../types/FormValues';
import useFormValues from '../hooks/useFormValues';
import { useLocation } from 'react-router-dom';
import useToast from '../hooks/useToast';
import InputContainer from '../components/InputContainer';
import { hasErrorMessage, hasNullValue } from '../validations';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import { DEFAULT_TOPIC_IMAGE, LAYOUT_PADDING, SIDEBAR } from '../constants';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import Modal from '../components/Modal';
import { styled } from 'styled-components';
import { ModalContext } from '../context/ModalContext';
import { getApi } from '../apis/getApi';
import Checkbox from '../components/common/CheckBox';
import { TagContext } from '../context/TagContext';
import { Member } from '../types/Topic';
import usePost from '../apiHooks/usePost';

type NewTopicFormValuesType = Omit<NewTopicFormProps, 'topics'>;

const NewTopic = () => {
  const { openModal, closeModal } = useContext(ModalContext);
  const { routePage } = useNavigator();
  const { state: pulledPinIds } = useLocation();
  const { showToast } = useToast();
  const { width } = useSetLayoutWidth(SIDEBAR);
  const { navbarHighlights: _ } = useSetNavbarHighlight('addMapOrPin');
  const { setTags } = useContext(TagContext);
  const { fetchPost } = usePost();
  const { formValues, errorMessages, onChangeInput } =
    useFormValues<NewTopicFormValuesType>({
      name: '',
      description: '',
      image: '',
    });

  const [isPrivate, setIsPrivate] = useState(false); // 혼자 볼 지도 :  같이 볼 지도
  const [isAll, setIsAll] = useState(true); // 모두 : 지정 인원
  const [members, setMembers] = useState<Member[]>([]);
  const [authorizedMemberIds, setAuthorizedMemberIds] = useState<number[]>([]);

  useEffect(() => {
    const getMemberData = async () => {
      const memberData = await getApi<any>(`/members`);
      setMembers(memberData);
    };

    getMemberData();
  }, []);

  const onChangeMemberChecked = (isChecked: boolean, id: number) => {
    setAuthorizedMemberIds((prev: Member['id'][]) =>
      isChecked ? [...prev, id] : prev.filter((n: number) => n !== id),
    );
  };

  const goToBack = () => {
    routePage(-1);
  };

  const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (hasErrorMessage(errorMessages) || hasNullValue(formValues, 'image')) {
      showToast('error', '입력하신 항목들을 다시 한 번 확인해주세요.');
      return;
    }

    const topicId = await createTopic();

    if (topicId) {
      await addAuthorityToTopicWithGroupPermission(topicId);
      routePage(`/topics/${topicId}`);
    }
  };

  const createTopic = async () => {
    const response = await postToServer();
    const location = response?.headers.get('Location');

    if (location) {
      const topicIdFromLocation = location.split('/')[2];
      return Number(topicIdFromLocation);
    }
  };

  const addAuthorityToTopicWithGroupPermission = async (topicId: number) => {
    if (isAll) return;

    fetchPost(
      {
        url: '/permissions',
        payload: {
          topicId,
          memberIds: isPrivate ? [] : authorizedMemberIds,
        },
      },
      `${formValues.name} 지도의 권한 설정에 실패했습니다.`,
    );
  };

  const postToServer = async () => {
    return fetchPost(
      {
        url: '/topics/new',
        payload: {
          image: formValues.image || DEFAULT_TOPIC_IMAGE,
          name: formValues.name,
          description: formValues.description,
          pins: pulledPinIds ? pulledPinIds.split(',') : [],
          publicity: isPrivate ? 'PRIVATE' : 'PUBLIC',
          permissionType: isAll && !isPrivate ? 'ALL_MEMBERS' : 'GROUP_ONLY',
        },
      },
      '지도 생성에 실패하였습니다. 입력하신 항목들을 다시 확인해주세요.',
      () => {
        showToast('info', `${formValues.name} 지도를 생성하였습니다.`);
      },
    );
  };

  return (
    <>
      <form onSubmit={onSubmit}>
        <Space size={4} />
        <Flex
          width={`calc(${width} - ${LAYOUT_PADDING})`}
          $flexDirection="column"
        >
          <Text color="black" $fontSize="large" $fontWeight="bold">
            지도 생성
          </Text>
          <Space size={5} />
          <InputContainer
            tagType="input"
            containerTitle="지도 이미지"
            isRequired={false}
            name="image"
            value={formValues.image}
            placeholder="이미지 URL을 입력해주세요."
            onChangeInput={onChangeInput}
            tabIndex={1}
            autoFocus
            errorMessage={errorMessages.image}
            maxLength={2048}
          />
          <Space size={1} />
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

          <Space size={1} />

          <Text color="black" $fontSize="default" $fontWeight="normal">
            지도 종류
          </Text>
          <Space size={1} />
          <Flex $alignItems="flex-start">
            <Flex width="108px" $alignItems="flex-start">
              <input
                type="radio"
                id="publicity-public"
                checked={!isPrivate}
                onChange={() => setIsPrivate(false)}
                tabIndex={4}
              />
              <Space size={1} />
              <label htmlFor="publicity-public">공개 지도</label>
            </Flex>

            <Space size={2} />

            <input
              type="radio"
              id="publicity-private"
              checked={isPrivate}
              onChange={() => setIsPrivate(true)}
              tabIndex={4}
            />
            <Space size={1} />
            <label htmlFor="publicity-private">비공개 지도</label>
          </Flex>

          <Space size={5} />
          <Space size={0} />

          <Text color="black" $fontSize="default" $fontWeight="normal">
            핀 생성 및 수정 권한 부여
          </Text>

          <Space size={1} />

          <Flex $alignItems="flex-start">
            <Flex width="108px" $alignItems="flex-start">
              <input
                type="radio"
                id="permission-all"
                checked={isAll}
                onChange={() => {
                  setIsAll(true);
                  setAuthorizedMemberIds([]);
                }}
                tabIndex={5}
              />
              <Space size={1} />
              {isPrivate ? (
                <label htmlFor="permission-all">혼자만</label>
              ) : (
                <label htmlFor="permission-all">모두에게</label>
              )}
            </Flex>

            <Space size={2} />

            <input
              type="radio"
              id="permission-group"
              checked={!isAll}
              onChange={() => {
                setIsAll(false);
                openModal('newTopic');
                setAuthorizedMemberIds([]);
              }}
              onClick={() => {
                isAll === false && openModal('newTopic');
              }}
              tabIndex={5}
            />
            <Space size={1} />
            <label htmlFor="permission-group">친구들에게</label>
          </Flex>

          {authorizedMemberIds.length > 0 && (
            <>
              <Space size={5} />
              <Space size={0} />
              <Box>
                <Text color="black" $fontSize="default" $fontWeight="normal">
                  선택한 친구들
                </Text>
                <Space size={1} />
                {members.map((member) => {
                  if (authorizedMemberIds.includes(member.id))
                    return (
                      <Text
                        color="black"
                        $fontSize="default"
                        $fontWeight="normal"
                        key={member.id}
                      >
                        • {member.nickName}
                      </Text>
                    );
                })}
              </Box>
            </>
          )}

          <Space size={6} />

          <Flex $justifyContent="end">
            <Button
              tabIndex={7}
              type="button"
              variant="secondary"
              onClick={goToBack}
            >
              취소하기
            </Button>
            <Space size={3} />
            <Button
              tabIndex={7}
              variant="primary"
              onClick={() => {
                setTags([]);
              }}
            >
              생성하기
            </Button>
          </Flex>
        </Flex>
      </form>

      <Modal
        modalKey="newTopic"
        position="center"
        width="400px"
        height="600px"
        overflow="scroll"
        $dimmedColor="rgba(0, 0, 0, 0.5)"
      >
        <ModalContentsWrapper>
          <Flex
            padding={'12px'}
            position="sticky"
            top="0"
            $justifyContent="space-between"
            $alignItems="center"
          >
            <Text $fontSize="large" $fontWeight="bold" color="black">
              멤버 선택
            </Text>
            <Text $fontSize="small" $fontWeight="normal" color="black">
              {authorizedMemberIds.length}명 선택됨
            </Text>
          </Flex>
          <Space size={2} />
          <CheckboxList>
            {members.map((member) => (
              <CheckboxListItem key={member.id}>
                <Checkbox
                  id={member.id}
                  isAlreadyChecked={authorizedMemberIds.includes(member.id)}
                  label={member.nickName}
                  onChecked={onChangeMemberChecked}
                />
              </CheckboxListItem>
            ))}
          </CheckboxList>

          <Space size={1} />

          <Flex $justifyContent="end" padding={'12px'} bottom="0px">
            <Button
              tabIndex={6}
              type="button"
              variant="secondary"
              onClick={() => {
                closeModal('newTopic');
                setIsAll(true);
                setAuthorizedMemberIds([]);
              }}
            >
              취소하기
            </Button>

            <Space size={3} />

            <Button
              tabIndex={6}
              variant="primary"
              onClick={() => {
                closeModal('newTopic');
              }}
            >
              선택하기
            </Button>
          </Flex>
          <Space size={2} />
        </ModalContentsWrapper>
      </Modal>
    </>
  );
};

const ModalContentsWrapper = styled.div`
  width: 100%;
  height: 100%;
  background-color: white;
  display: flex;
  flex-direction: column;
`;

const CheckboxList = styled.div`
  flex: 1;
  overflow-y: scroll;
`;

const CheckboxListItem = styled.div`
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  margin-bottom: 1rem;
  padding: 1rem;
  border-radius: 5px;
  background-color: white;

  &:last-child {
    margin-bottom: 0;
    border-bottom: none;
  }

  &:hover {
    background-color: #f8f9fa;
  }
`;

export default NewTopic;
