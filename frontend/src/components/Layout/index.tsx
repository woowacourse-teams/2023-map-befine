import { useEffect, useRef, useState } from 'react';
import Map from '../Map';
import Flex from '../common/Flex';
import Input from '../common/Input';
import Space from '../common/Space';
import Logo from './Logo';
import CoordinatesProvider from '../../context/CoordinatesContext';
import MarkerProvider from '../../context/MarkerContext';

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
      center: new Tmapv2.LatLng(37.5055, 127.0509),
    });

    setMap(map);
    return () => {
      map.destroy();
    };
  }, []);

  return (
    <CoordinatesProvider>
      <MarkerProvider>
        <Flex height="100vh" width="100vw">
          <Flex
            $flexDirection="column"
            width="400px"
            height="100vh"
            $backgroundColor="white"
            padding={4}
          >
            <Logo />
            <Space size={5} />
            <Input placeholder="검색어를 입력하세요." />
            <Flex
              height="calc(100vh - 120px)"
              $flexDirection="column"
              overflow="auto"
            >
              {children}
            </Flex>
          </Flex>
          <Map ref={mapContainer} map={map} />
        </Flex>
      </MarkerProvider>
    </CoordinatesProvider>
  );
};

export default Layout;
