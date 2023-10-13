import styled from 'styled-components';

import BannerItemBoongWEBP from '../../assets/banner_boong.webp';
import BannerItemUsageWEBP from '../../assets/banner_usage.webp';
import useNavigator from '../../hooks/useNavigator';
import Box from '../common/Box';
import Swiper from '../common/Swiper';
import Tab from '../common/Swiper/Tab';

const USAGE_URL =
  'https://yoondgu.notion.site/3e5b3c98c4814aa1bd5887104fee314e?pvs=4';

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
