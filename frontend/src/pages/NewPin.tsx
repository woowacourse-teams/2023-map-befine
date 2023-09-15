import Input from '../components/common/Input';
import Text from '../components/common/Text';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Button from '../components/common/Button';
import { postApi, postFormApi } from '../apis/postApi';
import { FormEvent, useContext, useEffect, useState } from 'react';
import { getApi } from '../apis/getApi';
import { TopicCardProps } from '../types/Topic';
import useNavigator from '../hooks/useNavigator';
import { NewPinFormProps } from '../types/FormValues';
import useFormValues from '../hooks/useFormValues';
import { MarkerContext } from '../context/MarkerContext';
import { CoordinatesContext } from '../context/CoordinatesContext';
import { useLocation } from 'react-router-dom';
import useToast from '../hooks/useToast';
import InputContainer from '../components/InputContainer';
import { hasErrorMessage, hasNullValue } from '../validations';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import { LAYOUT_PADDING, SIDEBAR } from '../constants';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import { ModalContext } from '../context/ModalContext';
import Modal from '../components/Modal';
import { styled } from 'styled-components';
import ModalMyTopicList from '../components/ModalMyTopicList';
import { getMapApi } from '../apis/getMapApi';

type NewPinFormValueType = Pick<
  NewPinFormProps,
  'name' | 'address' | 'description'
>;

const NewPin = () => {
  const { state: topicId } = useLocation();
  const { navbarHighlights: _ } = useSetNavbarHighlight('addMapOrPin');
  const [topic, setTopic] = useState<any>(null);
  const [selectedTopic, setSelectedTopic] = useState<any>(null);
  const [showedImages, setShowedImages] = useState<string[]>([]);
  const { clickedMarker } = useContext(MarkerContext);
  const { clickedCoordinate, setClickedCoordinate } =
    useContext(CoordinatesContext);
  const { formValues, errorMessages, onChangeInput } =
    useFormValues<NewPinFormValueType>({
      name: '',
      address: '',
      description: '',
    });
  const { routePage } = useNavigator();
  const { showToast } = useToast();
  const { width } = useSetLayoutWidth(SIDEBAR);
  const { openModal, closeModal } = useContext(ModalContext);

  const [formImages, setFormImages] = useState<File[]>([]);
  const formData = new FormData();

  const goToBack = () => {
    routePage(-1);
  };

  const postToServer = async () => {
    let postTopicId = topic?.id;
    let postName = formValues.name;

    const formData = new FormData();

    if (!topic) {
      //토픽이 없으면 selectedTopic을 통해 토픽을 생성한다.
      postTopicId = selectedTopic?.topicId;
    }

    if (topic?.length > 1) {
      postTopicId = selectedTopic.topicId;
    }

    if (!formImages) return;
    formImages.forEach((file) => {
      formData.append('images', file);
    });

    const objectData = {
      topicId: postTopicId,
      name: postName,
      address: clickedCoordinate.address,
      description: formValues.description,
      latitude: clickedCoordinate.latitude,
      longitude: clickedCoordinate.longitude,
      legalDongCode: '',
    };

    const data = JSON.stringify(objectData);
    const jsonBlob = new Blob([data], { type: 'application/json' });

    formData.append('request', jsonBlob);

    await postFormApi('/pins', formData);
  };

  const onSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (clickedCoordinate.address?.length === 0) {
      showToast('error', '장소의 위치를 입력해주세요.');
      return;
    }

    if (hasErrorMessage(errorMessages) || hasNullValue(formValues, 'address')) {
      showToast('error', '입력하신 항목들을 다시 한 번 확인해주세요.');
      return;
    }

    try {
      await postToServer();

      showToast('info', `${formValues.name} 핀을 추가하였습니다.`);

      // 선택된 마커가 있으면 마커를 지도에서 제거
      if (clickedMarker) {
        clickedMarker.setMap(null);
      }

      setClickedCoordinate({
        latitude: 0,
        longitude: 0,
        address: '',
      });

      if (topicId && topicId.length > 1) {
        routePage(`/topics/${topicId}`, [topic!.id]);
        return;
      }
      let postTopicId = topic?.id;
      let postName = formValues.name;

      if (!topic) {
        //토픽이 없으면 selectedTopic을 통해 토픽을 생성한다.
        postTopicId = selectedTopic?.topicId;
        postName = selectedTopic?.topicName;
      }

      if (postTopicId) routePage(`/topics/${postTopicId}`, [postTopicId]);
    } catch {
      showToast(
        'error',
        '핀을 추가할 수 있는 권한이 없거나 지도를 선택하지 않았습니다.',
      );
    }
  };

  const onClickAddressInput = (
    e:
      | React.MouseEvent<HTMLInputElement>
      | React.KeyboardEvent<HTMLInputElement>,
  ) => {
    if (!(e.type === 'click') && e.currentTarget.value) return;

    var width = 500; //팝업의 너비
    var height = 600; //팝업의 높이
    new window.daum.Postcode({
      width: width, //생성자에 크기 값을 명시적으로 지정해야 합니다.
      height: height,
      onComplete: async function (data: any) {
        const addr = data.roadAddress; // 주소 변수

        //data를 통해 받아온 값을 Tmap api를 통해 위도와 경도를 구한다.
        const { ConvertAdd } = await getMapApi<any>(
          `https://apis.openapi.sk.com/tmap/geo/convertAddress?version=1&format=json&callback=result&searchTypCd=NtoO&appKey=P2MX6F1aaf428AbAyahIl9L8GsIlES04aXS9hgxo&coordType=WGS84GEO&reqAdd=${addr}`,
        );
        const lat = ConvertAdd.oldLat;
        const lng = ConvertAdd.oldLon;

        setClickedCoordinate({
          latitude: lat,
          longitude: lng,
          address: addr,
        });
      },
    }).open({
      popupKey: 'postPopUp',
    });
  };

  const onPinImageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const imageLists = event.target.files;
    let imageUrlLists = [...showedImages];

    const file = event.target.files && event.target.files[0];

    if (!file) {
      showToast('error', 'No file selected');
      return;
    }

    if (!imageLists) return;

    for (let i = 0; i < imageLists.length; i++) {
      const currentImageUrl = URL.createObjectURL(imageLists[i]);
      imageUrlLists.push(currentImageUrl);
    }

    if (imageUrlLists.length > 8) {
      showToast(
        'error',
        '이미지 개수는 최대 8개까지만 선택 가능합니다. 다시 선택해 주세요.',
      );
      imageUrlLists = imageUrlLists.slice(0, 8);
      return;
    }

    setFormImages([...formImages, file]);
    setShowedImages(imageUrlLists);
  };

  useEffect(() => {
    const getTopicId = async () => {
      if (topicId && topicId.split(',').length === 1) {
        const data = await getApi<TopicCardProps>(`/topics/${topicId}`);

        setTopic(data);
      }

      if (topicId && topicId.split(',').length > 1) {
        const topics = await getApi<any>(`/topics/ids?ids=${topicId}`);

        setTopic(topics);
      }
    };

    getTopicId();
  }, []);

  return (
    <>
      <form onSubmit={onSubmit}>
        <Space size={4} />
        <Wrapper
          width={`calc(${width} - ${LAYOUT_PADDING})`}
          $flexDirection="column"
        >
          <Text color="black" $fontSize="large" $fontWeight="bold">
            핀 생성
          </Text>

          <Space size={5} />

          <Flex>
            <Text color="black" $fontSize="default" $fontWeight="normal">
              지도 선택
            </Text>
            <Space size={0} />
            <Text color="primary" $fontSize="extraSmall" $fontWeight="normal">
              *
            </Text>
          </Flex>
          <Space size={0} />
          <Button
            type="button"
            variant="primary"
            onClick={() => {
              if (topic && topic.name) return;
              openModal('newPin');
            }}
          >
            {topic?.name
              ? topic?.name
              : selectedTopic?.topicName
              ? selectedTopic?.topicName
              : '지도를 선택해주세요.'}
          </Button>

          <Space size={5} />

          <Text color="black" $fontSize="default" $fontWeight="normal">
            사진 선택
          </Text>
          <Space size={0} />
          <Flex>
            <ImageInputLabel htmlFor="file">파일찾기</ImageInputLabel>
            <input
              id="file"
              type="file"
              name="images"
              onChange={onPinImageChange}
              style={{ display: 'none' }}
              multiple
            />
          </Flex>
          <Space size={0} />
          <Flex $flexDirection="row" $flexWrap="wrap">
            {showedImages.map((image, id) => (
              <div key={id}>
                <ShowImage src={image} alt={`${image}-${id}`} />
                <Space size={0} />
              </div>
            ))}
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

          <Flex>
            <Text color="black" $fontSize="default" $fontWeight="normal">
              장소 위치
            </Text>
            <Space size={0} />
            <Text color="primary" $fontSize="extraSmall" $fontWeight="normal">
              *
            </Text>
          </Flex>
          <Space size={0} />
          <Input
            name="address"
            readOnly
            value={clickedCoordinate.address}
            onClick={onClickAddressInput}
            onKeyDown={onClickAddressInput}
            tabIndex={2}
            placeholder="지도를 클릭하거나 장소의 위치를 입력해주세요."
          />

          <Space size={5} />

          <InputContainer
            tagType="textarea"
            containerTitle="장소 설명"
            isRequired={true}
            name="description"
            value={formValues.description}
            placeholder="1000자 이내로 장소에 대한 의견을 남겨주세요."
            onChangeInput={onChangeInput}
            tabIndex={3}
            errorMessage={errorMessages.description}
            maxLength={1000}
          />

          <Space size={6} />

          <Flex $justifyContent="end">
            <Button
              tabIndex={5}
              type="button"
              variant="secondary"
              onClick={goToBack}
            >
              취소하기
            </Button>
            <Space size={3} />
            <Button tabIndex={4} variant="primary">
              추가하기
            </Button>
          </Flex>
        </Wrapper>
      </form>

      <Modal
        modalKey="newPin"
        position="center"
        width="744px"
        height="512px"
        $dimmedColor="rgba(0,0,0,0.25)"
      >
        <ModalContentsWrapper>
          <Space size={5} />
          <Text color="black" $fontSize="extraLarge" $fontWeight="bold">
            내 토픽 리스트
          </Text>
          <Text color="gray" $fontSize="small" $fontWeight="normal">
            핀을 저장할 지도를 선택해주세요.
          </Text>
          <Space size={4} />
          <ModalMyTopicList topicId={topicId} topicClick={setSelectedTopic} />
        </ModalContentsWrapper>
      </Modal>
    </>
  );
};

const Wrapper = styled(Flex)`
  margin: 0 auto;

  @media (max-width: 1076px) {
    width: calc(50vw - 40px);
  }

  @media (max-width: 744px) {
    width: ${({ width }) => width};
    margin: 0 auto;
  }
`;

const ModalContentsWrapper = styled.div`
  width: 100%;
  height: 100%;
  background-color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
  overflow: scroll;
`;

const ImageInputLabel = styled.label`
  height: 40px;
  margin-left: 10px;
  padding: 10px 10px;

  color: ${({ theme }) => theme.color.black};
  background-color: ${({ theme }) => theme.color.lightGray};

  font-size: ${({ theme }) => theme.fontSize.extraSmall};
  cursor: pointer;
`;

const ShowImage = styled.img`
  width: 80px;
  height: 80px;
`;

export default NewPin;
