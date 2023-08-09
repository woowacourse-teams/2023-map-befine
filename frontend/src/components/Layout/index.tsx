import { useContext, useEffect, useRef, useState } from 'react';
import Map from '../Map';
import Flex from '../common/Flex';
import Logo from './Logo';
import CoordinatesProvider from '../../context/CoordinatesContext';
import MarkerProvider from '../../context/MarkerContext';
import ToastProvider from '../../context/ToastContext';
import Toast from '../Toast';
import { styled } from 'styled-components';
import { LayoutWidthContext } from '../../context/LayoutWidthContext';
import SeeTogetherProvider from '../../context/SeeTogetherContext';
import Navbar from './Navbar';
import Back from '../../assets/Back.svg';

type LayoutProps = {
  children: React.ReactNode;
};

declare global {
  interface Window {
    Tmapv3: any;
    daum: any;
  }
}

const Layout = ({ children }: LayoutProps) => {
  const { Tmapv3 } = window;
  const mapContainer = useRef(null);
  const { width } = useContext(LayoutWidthContext);

  const [map, setMap] = useState(null);

  useEffect(() => {
    const map = new Tmapv3.Map(mapContainer.current, {
      center: new Tmapv3.LatLng(37.5154, 127.1029),
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
          <SeeTogetherProvider>
            <Flex height="100vh" width="100vw" overflow="hidden">
              <LayoutFlex
                $flexDirection="column"
                $minWidth={width}
                height="100vh"
                $backgroundColor="white"
              >
                <Flex $flexDirection="column" padding="20px 20px 0 20px">
                  <Logo />
                  <Space size={4} />
                </Flex>
                <Flex
                  height="calc(100vh - 40px)"
                  $flexDirection="column"
                  overflow="auto"
                  padding="0 20px 20px 20px"
                >
                  {children}
                </Flex>
              </LayoutFlex>
              <Navbar />
              <Toast />
              <Map ref={mapContainer} map={map} $minWidth={width} />
            </Flex>
          </SeeTogetherProvider>
        </MarkerProvider>
      </CoordinatesProvider>
    </ToastProvider>
  );
};

const LayoutFlex = styled(Flex)`
  transition: all ease 0.3s;
`;

export default Layout;
