import Input from '../components/common/Input';
import Text from '../components/common/Text';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Button from '../components/common/Button';
import { postApi } from '../apis/postApi';
import { FormEvent, useContext, useEffect, useRef, useState } from 'react';
import { getApi } from '../apis/getApi';
import { TopicType } from '../types/Topic';
import useNavigator from '../hooks/useNavigator';
import { NewPinValuesType } from '../types/FormValues';
import useFormValues from '../hooks/useFormValues';
import { MarkerContext } from '../context/MarkerContext';
import { CoordinatesContext } from '../context/CoordinatesContext';
import { useLocation } from 'react-router-dom';
import useToast from '../hooks/useToast';
import InputContainer from '../components/InputContainer';

const NewPin = () => {
  const { state: prevUrl } = useLocation();
  const [topic, setTopic] = useState<TopicType | null>(null);
  const { clickedMarker } = useContext(MarkerContext);
  const { clickedCoordinate, setClickedCoordinate } =
    useContext(CoordinatesContext);
  const { formValues, errorMessages, onChangeInput } = useFormValues<
    Pick<NewPinValuesType, 'name' | 'address' | 'description'>
  >({
    name: '',
    address: '',
    description: '',
  });
  const { routePage } = useNavigator();
  const { showToast } = useToast();
  const addressRef = useRef<HTMLInputElement | null>(null);

  const goToBack = () => {
    routePage(-1);
  };

  const postToServer = async () => {
    await postApi('/pins', {
      topicId: topic?.id || 'error',
      name: formValues.name,
      address: addressRef.current?.value,
      description: formValues.description,
      latitude: clickedCoordinate.latitude,
      longitude: clickedCoordinate.longitude,
      legalDongCode: '',
      images: [],
    });
  };

  const onSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (
      Object.values(errorMessages).some(
        (errorMessage) => errorMessage.length > 0,
      )
    ) {
      showToast('error', '입력하신 항목들을 다시 한 번 확인해주세요.');
      return;
    }

    await postToServer();

    if (prevUrl.length > 1) routePage(`/topics/${prevUrl}`, [topic!.id]);
    else routePage(`/topics/${topic?.id}`, [topic!.id]);

    showToast('info', `${formValues.name} 핀을 추가하였습니다.`);

    // 선택된 마커가 있으면 마커를 지도에서 제거
    if (clickedMarker) {
      clickedMarker.setMap(null);
    }

    setClickedCoordinate({
      latitude: '',
      longitude: '',
      address: '',
    });
  };

  const onClickAddressInput = () => {
    var width = 500; //팝업의 너비
    var height = 600; //팝업의 높이
    new window.daum.Postcode({
      width: width, //생성자에 크기 값을 명시적으로 지정해야 합니다.
      height: height,
      onComplete: async function (data: any) {
        const addr = data.roadAddress; // 주소 변수

        //data를 통해 받아온 값을 Tmap api를 통해 위도와 경도를 구한다.
        const { ConvertAdd } = await getApi(
          'tMap',
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

  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);

    const getTopicId = async () => {
      if (queryParams.has('topic-id')) {
        const topicId = queryParams.get('topic-id');
        const data = await getApi('default', `/topics/${topicId}`);
        setTopic(data);
      }
    };

    formValues.address = '';
    getTopicId();
  }, []);

  if (!topic) return <></>;

  return (
    <form onSubmit={onSubmit}>
      <Space size={4} />
      <Flex $flexDirection="column">
        <Text color="black" $fontSize="large" $fontWeight="bold">
          핀 추가
        </Text>

        <Space size={5} />

        <section>
          <Flex>
            <Text color="black" $fontSize="default" $fontWeight="normal">
              토픽 선택
            </Text>
            <Space size={0} />
            <Text color="primary" $fontSize="extraSmall" $fontWeight="normal">
              *
            </Text>
          </Flex>
          <Space size={0} />
          <Button type="button" variant="primary">{`${topic.name}`}</Button>
        </section>

        <Space size={5} />

        <InputContainer
          tagType="input"
          containerTitle="장소 이름"
          isRequired={true}
          name="name"
          value={formValues.name}
          placeholder="지도를 클릭하거나 장소의 이름을 입력해주세요."
          onChangeInput={onChangeInput}
          tabIndex={1}
          errorMessage={errorMessages.name}
          autoFocus
        />

        <Space size={1} />

        <section>
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
            ref={addressRef}
            tabIndex={2}
            placeholder="지도를 클릭하거나 장소의 위치를 입력해주세요."
          />
        </section>

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
      </Flex>
    </form>
  );
};

export default NewPin;
