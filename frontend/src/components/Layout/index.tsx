import { useContext, useEffect } from 'react';
import { css, styled } from 'styled-components';

import CoordinatesProvider from '../../context/CoordinatesContext';
import ImageModalContext from '../../context/ImageModalContext';
import { LayoutWidthContext } from '../../context/LayoutWidthContext';
import MarkerProvider from '../../context/MarkerContext';
import ModalProvider from '../../context/ModalContext';
import { NavbarHighlightsContext } from '../../context/NavbarHighlightsContext';
import SeeTogetherProvider from '../../context/SeeTogetherContext';
import TagProvider from '../../context/TagContext';
import ToastProvider from '../../context/ToastContext';
import Box from '../common/Box';
import Flex from '../common/Flex';
import Space from '../common/Space';
import Map from '../Map';
import Toast from '../Toast';
import Logo from './Logo';
import Navbar from './Navbar';

type LayoutProps = {
  children: React.ReactNode;
};

const initViewPortHeight = () => {
  const vh = window.innerHeight * 0.01;
  document.documentElement.style.setProperty('--vh', `${vh}px`);
};

function Layout({ children }: LayoutProps) {
  const { width } = useContext(LayoutWidthContext);
  const { navbarHighlights } = useContext(NavbarHighlightsContext);

  useEffect(() => {
    initViewPortHeight();

    window.addEventListener('resize', initViewPortHeight);

    return () => {
      window.removeEventListener('resize', initViewPortHeight);
    };
  }, []);

  return (
    <ToastProvider>
      <ModalProvider>
        <ImageModalContext>
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
                      height="calc(var(--vh, 1vh) * 100)"
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
                        $flexDirection="column"
                        height="inherit"
                        overflow="auto"
                      >
                        {children}
                      </Flex>
                      <Navbar $layoutWidth={width} />
                      <Toast />
                    </LayoutFlex>
                    <Map />
                  </MediaWrapper>
                </TagProvider>
              </SeeTogetherProvider>
            </MarkerProvider>
          </CoordinatesProvider>
        </ImageModalContext>
      </ModalProvider>
    </ToastProvider>
  );
}

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
        width: max-content;
        background-color: transparent;
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
  height: calc(var(--vh, 1vh) * 100);

  @media (max-width: 1076px) {
    flex-direction: ${({ $layoutWidth }) => {
      if ($layoutWidth === '372px') return 'column-reverse';
    }};
  }
`;

const LayoutFlex = styled(Flex)<{ $layoutWidth: '372px' | '100vw' }>`
  transition: all ease 0.3s;

  @media (max-width: 1076px) {
    height: ${({ $layoutWidth }) =>
      $layoutWidth === '372px' && 'calc(var(--vh, 1vh) * 50)'};
    transition: none;
  }
`;
export default Layout;
