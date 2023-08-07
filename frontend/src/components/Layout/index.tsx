import { useEffect, useRef, useState } from 'react';
import Map from '../Map';
import Flex from '../common/Flex';
import Input from '../common/Input';
import Space from '../common/Space';
import Logo from './Logo';
import CoordinatesProvider from '../../context/CoordinatesContext';
import MarkerProvider from '../../context/MarkerContext';
import ToastProvider from '../../context/ToastContext';
import Toast from '../Toast';

type LayoutProps = {
  children: React.ReactNode;
};

declare global {
  interface Window {
    Tmapv2: any;
    daum: any;
  }
}

const Layout = ({ children }: LayoutProps) => {
  const { Tmapv2 } = window;
  const mapContainer = useRef(null);

  const [map, setMap] = useState(null);

  useEffect(() => {
    const map = new Tmapv2.Map(mapContainer.current, {
      center: new Tmapv2.LatLng(37.5154, 127.1029),
    });
    map.setZoomLimit(8, 17);
    setMap(map);
    return () => {
      map.destroy();
    };
  }, []);

  return (
    <ToastProvider>
      <CoordinatesProvider>
        <MarkerProvider>
          <Flex height="100vh" width="100vw">
            <Flex
              $flexDirection="column"
              $minWidth="400px"
              height="100vh"
              $backgroundColor="white"
            >
              <Flex $flexDirection="column" padding="20px 20px 0 20px">
                <Logo />
                <Space size={5} />
                <Input
                  placeholder="검색어를 입력하세요."
                  aria-label="검색어 입력창"
                />
              </Flex>
              <Flex
                height="calc(100vh - 120px)"
                $flexDirection="column"
                overflow="auto"
                padding="0 20px 20px 20px"
              >
                {children}
              </Flex>
            </Flex>
            <Toast />
            <Map ref={mapContainer} map={map} />
          </Flex>
        </MarkerProvider>
      </CoordinatesProvider>
    </ToastProvider>
  );
};

export default Layout;
