import Input from '../components/common/Input';
import Text from '../components/common/Text';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Button from '../components/common/Button';
import Textarea from '../components/common/Textarea';
import { postApi } from '../utils/postApi';
import { FormEvent, useContext, useEffect, useRef, useState } from 'react';
import { getApi } from '../utils/getApi';
import { getAddress } from '../utils/getAddress';
import { TopicType } from '../types/Topic';
import useNavigator from '../hooks/useNavigator';
import { NewPinValuesType } from '../types/FormValues';
import useFormValues from '../hooks/useFormValues';
import { MarkerContext } from '../context/MarkerContext';
import { CoordinatesContext } from '../context/CoordinatesContext';

const NewPin = () => {
  const [topic, setTopic] = useState<TopicType | null>(null);
  const { markers, clickedMarker } = useContext(MarkerContext);
  const { clickedCoordinate, setClickedCoordinate } =
    useContext(CoordinatesContext);
  const { formValues, setFormValues, onChangeInput } =
    useFormValues<NewPinValuesType>({
      topicId: 0,
      name: '',
      address: '',
      description: '',
      latitude: '',
      longitude: '',
      legalDongCode: '',
      images: [],
    });
  const { routePage } = useNavigator();
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

    await postToServer();
    routePage(`/topics/${topic?.id}`, [topic!.id]);
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
        const { ConvertAdd } = await getAddress(
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
    }).open({});
  };

  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);

    const getTopicId = async () => {
      if (queryParams.has('topic-id')) {
        const topicId = queryParams.get('topic-id');
        const data = await getApi(`/topics/${topicId}`);
        setTopic(data);
      }
    };

    formValues.address = '';
    getTopicId();
  }, []);

  if (!topic) return <></>;

  return (
    <form onSubmit={onSubmit}>
      <Space size={6} />
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

        <section>
          <Flex>
            <Text color="black" $fontSize="default" $fontWeight="normal">
              장소 이름
            </Text>
            <Space size={0} />
            <Text color="primary" $fontSize="extraSmall" $fontWeight="normal">
              *
            </Text>
          </Flex>
          <Space size={0} />
          <Input
            name="name"
            value={formValues.name}
            onChange={onChangeInput}
            placeholder="지도를 클릭하거나 장소의 이름을 입력해주세요."
            autoFocus={true}
          />
        </section>

        <Space size={5} />

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
            ref={addressRef}
            placeholder="지도를 클릭하거나 장소의 위치를 입력해주세요."
          />
        </section>

        <Space size={5} />

        <section>
          <Flex>
            <Text color="black" $fontSize="default" $fontWeight="normal">
              장소 설명
            </Text>
            <Space size={0} />
            <Text color="primary" $fontSize="extraSmall" $fontWeight="normal">
              *
            </Text>
          </Flex>
          <Space size={0} />
          <Textarea
            name="description"
            value={formValues.description}
            onChange={onChangeInput}
            placeholder="장소에 대한 의견을 자유롭게 남겨주세요."
          />
        </section>

        <Space size={6} />

        <Flex $justifyContent="end">
          <Button type="button" variant="secondary" onClick={goToBack}>
            취소하기
          </Button>
          <Space size={3} />
          <Button variant="primary">추가하기</Button>
        </Flex>
      </Flex>
    </form>
  );
};

export default NewPin;
