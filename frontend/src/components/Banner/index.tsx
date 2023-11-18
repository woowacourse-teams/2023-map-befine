import { Swiper, Tab } from 'map-befine-swiper';
import styled from 'styled-components';

import BannerItemBoongWEBP from '../../assets/banner_boong.webp';
import BannerItemUsageWEBP from '../../assets/banner_usage.webp';
import useNavigator from '../../hooks/useNavigator';
import Box from '../common/Box';

const USAGE_URL =
  'https://sites.google.com/woowahan.com/woowacourse-demo-5th/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8/%EA%B4%9C%EC%B0%AE%EC%9D%84%EC%A7%80%EB%8F%84';

export default function Banner() {
  const { routePage } = useNavigator();

  const goToBoongABbangTopic = () => {
    if (process.env.APP_URL === 'https://mapbefine.kro.kr/api') {
      routePage('/topics/1', 1);
      return;
    }

    routePage('/topics/62', 62);
  };

  return (
    <Swiper
      width={1140}
      height={400}
      $simpleTab
      $tabBoxPosition="bottom"
      $isNotTabBoxShow
      swipeable
      swiper
      autoplay
    >
      <Tab label="붕어빵 지도">
        <Box cursor="pointer" onClick={goToBoongABbangTopic}>
          <BannerImage
            width="100%"
            src={BannerItemBoongWEBP}
            alt="붕어빵 배너"
          />
        </Box>
      </Tab>
      <Tab label="사용법">
        <Box cursor="pointer">
          <a href={USAGE_URL} target="blank">
            <BannerImage src={BannerItemUsageWEBP} alt="사용법 배너" />
          </a>
        </Box>
      </Tab>
    </Swiper>
  );
}

const BannerImage = styled.img`
  width: 100%;
  min-height: 200px;
`;
