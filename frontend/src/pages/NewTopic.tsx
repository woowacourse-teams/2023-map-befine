import { useContext, useEffect, useState } from 'react';
import Text from '../components/common/Text';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Button from '../components/common/Button';
import { postApi } from '../apis/postApi';
import useNavigator from '../hooks/useNavigator';
import { NewTopicFormProps } from '../types/FormValues';
import useFormValues from '../hooks/useFormValues';
import { useLocation } from 'react-router-dom';
import useToast from '../hooks/useToast';
import InputContainer from '../components/InputContainer';
import { hasErrorMessage, hasNullValue } from '../validations';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import { LAYOUT_PADDING, SIDEBAR } from '../constants';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import Modal from '../components/Modal';
import { styled } from 'styled-components';
import { ModalContext } from '../context/ModalContext';
import { getApi } from '../apis/getApi';
import { Member } from '../types/Login';
import Checkbox from '../components/common/CheckBox';

type NewTopicFormValuesType = Omit<NewTopicFormProps, 'topics'>;

const DEFAULT_IMAGE =
  'https://velog.velcdn.com/images/semnil5202/post/37dae18f-9860-4483-bad5-1158a210e5a8/image.svg';

const NewTopic = () => {
  const [isPrivate, setIsPrivate] = useState(false); // 혼자 볼 지도 :  같이 볼 지도
  const [isAll, setIsAll] = useState(true); // 모두 : 지정 인원
  const { openModal, closeModal } = useContext(ModalContext);
  const { formValues, errorMessages, onChangeInput } =
    useFormValues<NewTopicFormValuesType>({
      name: '',
      description: '',
      image: '',
    });
  const { routePage } = useNavigator();
  const { state: taggedIds } = useLocation();
  const { showToast } = useToast();
  const { width } = useSetLayoutWidth(SIDEBAR);
  const { navbarHighlights: _ } = useSetNavbarHighlight('addMapOrPin');

  const [members, setMembers] = useState<Member[]>([]);

  useEffect(() => {
    const getMemberData = async () => {
      const memberData = await getApi<any>('default', `/members`);
      setMembers(memberData);
    };

    getMemberData();
  }, []);

  //해당 토픽에 권한을 부여할 아이디들을 담는 state
  //addAuthority에 인자로 넘겨줌
  const [checkedMemberIds, setCheckedMemberIds] = useState<number[]>([]);

  const handleChecked = (isChecked: boolean, id: number) =>
    setCheckedMemberIds((prev: Member['id'][]) =>
      isChecked ? [...prev, id] : prev.filter((n: number) => n !== id),
    );

  const goToBack = () => {
    routePage(-1);
  };

  const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (hasErrorMessage(errorMessages) || hasNullValue(formValues, 'image')) {
      showToast('error', '입력하신 항목들을 다시 한 번 확인해주세요.');
      return;
    }

    if (isPrivate) {
      const topicId = await postToServer();

      const result = await addAuthority(topicId);
      if (topicId) routePage(`/topics/${topicId}`);
      return;
    }

    if (!isPrivate && !isAll) {
      const topicId = await postToServer();

      const result = await addAuthority(topicId);
      if (topicId) routePage(`/topics/${topicId}`);
      return;
    }

    if (!isAll && checkedMemberIds.length === 0) {
      showToast('error', '멤버를 선택해주세요.');
      return;
    }

    //생성하기 버튼 눌렀을 때 postToServer로 TopicId 받고, 받은 topicId로 권한 추가
    try {
      const topicId = await postToServer();

      if (topicId) routePage(`/topics/${topicId}`);
    } catch {
      showToast(
        'error',
        '지도 생성을 실패하였습니다. 잠시 후 다시 시도해주세요.',
      );
    }
  };

  const postToServer = async () => {
    const response =
      taggedIds?.length > 1 && typeof taggedIds !== 'string'
        ? await mergeTopics()
        : await createTopic();
    const location = response.headers.get('Location');

    if (location) {
      const topicIdFromLocation = location.split('/')[2];
      return topicIdFromLocation;
    }
  };

  //header의 location으로 받아온 topicId에 권한 추가 기능
  const addAuthority = async (topicId: any) => {
    console.log('ADDAUTHORITY1');
    if (isAll && !isPrivate) return; // 모두 권한 준거면 return
    console.log('ADDAUTHORITY2');

    const response = await postApi(`/permissions`, {
      topicId: topicId,
      memberIds: checkedMemberIds,
    });
    return response;
  };

  const mergeTopics = async () => {
    showToast('info', `${formValues.name} 토픽을 병합하였습니다.`);

    return await postApi('/topics/merge', {
      image: formValues.image || DEFAULT_IMAGE,
      name: formValues.name,
      description: formValues.description,
      topics: taggedIds,
      publicity: isPrivate ? 'PRIVATE' : 'PUBLIC',
      permissionType: isAll ? 'ALL_MEMBERS' : 'GROUP_ONLY',
    });
  };

  const createTopic = async () => {
    showToast('info', `${formValues.name} 토픽을 생성하였습니다.`);
    const response = await postApi('/topics/new', {
      image: formValues.image || DEFAULT_IMAGE,
      name: formValues.name,
      description: formValues.description,
      pins: typeof taggedIds === 'string' ? taggedIds.split(',') : [],
      publicity: isPrivate ? 'PRIVATE' : 'PUBLIC',
      permissionType: isPrivate
        ? 'GROUP_ONLY'
        : isAll
        ? 'ALL_MEMBERS'
        : 'GROUP_ONLY',
    });
    return response;
  };

  return (
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
          공개 여부
        </Text>
        <Space size={1} />
        <Flex>
          <div>
            <input
              type="radio"
              id="public"
              name="accessibility"
              value="public"
              checked={!isPrivate}
              onChange={() => setIsPrivate(false)}
              tabIndex={4}
            />
            <label htmlFor="public">공개 지도</label>
          </div>
          <Space size={2} />
          <div>
            <input
              type="radio"
              id="private"
              name="accessibility"
              value="private"
              checked={isPrivate}
              onChange={() => setIsPrivate(true)}
              tabIndex={4}
            />
            <label htmlFor="private">비공개 지도</label>
          </div>
        </Flex>

        <Space size={5} />
        <Space size={0} />
        <Text color="black" $fontSize="default" $fontWeight="normal">
          핀 생성 및 수정 권한
        </Text>
        <Space size={1} />
        <Flex>
          <div>
            <input
              type="radio"
              id="ALL_MEMBERS"
              name="pinAuthority"
              value="all"
              checked={isAll}
              onChange={() => {
                setIsAll(true);
              }}
              tabIndex={5}
            />
            {isPrivate ? (
              <label htmlFor="ALL_MEMBERS">혼자</label>
            ) : (
              <label htmlFor="ALL_MEMBERS">모두</label>
            )}
          </div>
          <Space size={2} />
          <div>
            <input
              type="radio"
              id="GROUP_ONLY"
              name="pinAuthority"
              value="some"
              checked={!isAll}
              onChange={() => {
                setIsAll(false);
                openModal('newTopic');
                setCheckedMemberIds([]);
              }}
              tabIndex={5}
            />
            <label htmlFor="GROUP_ONLY">특정 인원 지정</label>
          </div>
        </Flex>

        <>
          <Modal
            modalKey="newTopic"
            position="center"
            width="400px"
            height="520px"
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
                  {checkedMemberIds.length}명 선택됨
                </Text>
              </Flex>
              <Space size={2} />
              <CheckboxList>
                {members.map((member) => (
                  <CheckboxListItem key={member.id}>
                    <Checkbox
                      id={member.id}
                      isAlreadyChecked={checkedMemberIds.includes(member.id)}
                      label={member.nickName}
                      onChecked={handleChecked}
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
                    setCheckedMemberIds([]);
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
          <Space size={7} />
          <Button tabIndex={7} variant="primary">
            생성하기
          </Button>
        </Flex>
      </Flex>
    </form>
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
