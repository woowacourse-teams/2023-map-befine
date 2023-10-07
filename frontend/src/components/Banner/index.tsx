import BannerItemBoongSVG from '../../assets/banner_boong.svg';
import BannerItemUsageSVG from '../../assets/banner_usage.svg';
import Swiper from '../common/Swiper';
import Tab from '../common/Swiper/Tab';

export default function Banner() {
  return (
    <Swiper
      width={1140}
      height={352}
      $simpleTab
      $tabBoxPosition="bottom"
      swipeable
      swiper
      autoplay
    >
      <Tab label="붕어빵 지도">
        <BannerItemBoongSVG width="100%" />
      </Tab>
      <Tab label="사용법">
        <BannerItemUsageSVG width="100%" />
      </Tab>
    </Swiper>
  );
}
