import { useContext, useEffect, useRef, useState } from 'react';
import Map from '../Map';
import Flex from '../common/Flex';
import Logo from './Logo';
import CoordinatesProvider from '../../context/CoordinatesContext';
import MarkerProvider from '../../context/MarkerContext';
import ToastProvider from '../../context/ToastContext';
import Toast from '../Toast';
import { css, styled } from 'styled-components';
import { LayoutWidthContext } from '../../context/LayoutWidthContext';
import SeeTogetherProvider from '../../context/SeeTogetherContext';
import Space from '../common/Space';
import Navbar from './Navbar';
import ModalProvider from '../../context/ModalContext';
import { NavbarHighlightsContext } from '../../context/NavbarHighlightsContext';
import TagProvider from '../../context/TagContext';
import Box from '../common/Box';

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
  const { navbarHighlights } = useContext(NavbarHighlightsContext);

  const [map, setMap] = useState(null);

  useEffect(() => {
    const map = new Tmapv3.Map(mapContainer.current, {
      center: new Tmapv3.LatLng(37.5154, 127.1029),
    });
    map.setZoomLimit(7, 18);
    setMap(map);
    return () => {
      map.destroy();
    };
  }, []);

  return (
    <ToastProvider>
      <ModalProvider>
        <CoordinatesProvider>
          <MarkerProvider>
            <SeeTogetherProvider>
              <TagProvider>
                <MediaWrapper
                  $isAddPage={navbarHighlights.addMapOrPin}
                  $layoutWidth={width}
                >
                  <LayoutFlex
                    $flexDirection="column"
                    $minWidth={width}
                    height="100vh"
                    $backgroundColor="white"
                    $layoutWidth={width}
                  >
                    <LogoWrapper $layoutWidth={width}>
                      <Box>
                        <Logo />
                        <Space size={2} />
                      </Box>
                    </LogoWrapper>
                    <Flex
                      height="calc(100vh - 48px)"
                      $flexDirection="column"
                      overflow="auto"
                      padding="0 20px 20px 20px"
                    >
                      {children}
                    </Flex>
                    <Navbar $layoutWidth={width} />
                  </LayoutFlex>
                  <Map ref={mapContainer} map={map} $minWidth={width} />
                </MediaWrapper>
                <Toast />
              </TagProvider>
            </SeeTogetherProvider>
          </MarkerProvider>
        </CoordinatesProvider>
      </ModalProvider>
    </ToastProvider>
  );
};

const LogoWrapper = styled.section<{
  $layoutWidth: '372px' | '100vw';
}>`
  width: 372px;
  display: flex;
  padding: 12px 20px 0 20px;

  @media (max-width: 1076px) {
    ${({ $layoutWidth }) =>
      $layoutWidth === '372px' &&
      css`
        width: 100vw;
        background-color: white;
        position: fixed;
        top: 0;
        z-index: 1;
      `};
  }
`;

const MediaWrapper = styled.section<{
  $isAddPage: boolean;
  $layoutWidth: '372px' | '100vw';
}>`
  display: flex;
  width: 100vw;
  overflow: hidden;

  @media (max-width: 1076px) {
    flex-direction: ${({ $isAddPage, $layoutWidth }) => {
      if ($isAddPage) return 'column';
      if ($layoutWidth === '372px') return 'column-reverse';
    }};
  }
`;

const LayoutFlex = styled(Flex)<{ $layoutWidth: '372px' | '100vw' }>`
  transition: all ease 0.3s;

  @media (max-width: 1076px) {
    height: ${({ $layoutWidth }) => $layoutWidth === '372px' && '50vh'};
    transition: none;
  }
`;

export default Layout;
