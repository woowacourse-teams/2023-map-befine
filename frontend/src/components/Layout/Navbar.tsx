import { useContext, useState } from 'react';
import { styled } from 'styled-components';
import useNavigator from '../../hooks/useNavigator';
import Flex from '../common/Flex';
import Button from '../common/Button';
import Space from '../common/Space';
import Text from '../common/Text';
import Home from '../../assets/nav_home.svg';
import SeeTogether from '../../assets/nav_seeTogether.svg';
import Favorite from '../../assets/nav_favorite.svg';
import Profile from '../../assets/nav_profile.svg';
import AddMapOrPin from '../../assets/nav_addMapOrPin.svg';
import FocusHome from '../../assets/nav_home_focus.svg';
import FocusSeeTogether from '../../assets/nav_seeTogether_focus.svg';
import FocusFavorite from '../../assets/nav_favorite_focus.svg';
import FocusAddMapOrPin from '../../assets/nav_addMapOrPin_focus.svg';
import FocusProfile from '../../assets/nav_profile_focus.svg';
import Modal from '../Modal';
import { ModalContext } from '../../context/ModalContext';
import { NavbarHighlightsContext } from '../../context/NavbarHighlightsContext';

interface NavBarProps {
  layoutWidth: '100vw' | '372px';
}

const Navbar = ({ layoutWidth }: NavBarProps) => {
  const { routePage } = useNavigator();
  const { openModal, closeModal } = useContext(ModalContext);
  const { navbarHighlights } = useContext(NavbarHighlightsContext);

  const goToHome = () => {
    routePage('/');
  };

  const goToSeeTogether = () => {
    routePage('/see-together');
  };

  const onClickAddMapOrPin = () => {
    openModal();
  };

  const goToFavorite = () => {
    routePage('/favorite');
  };

  const goToProfile = () => {
    routePage('/my-page');
  };

  return (
    <Wrapper layoutWidth={layoutWidth}>
      <IconWrapper onClick={goToHome}>
        {navbarHighlights.home ? <FocusHome /> : <Home />}
        <Text
          color={navbarHighlights.home ? 'primary' : 'darkGray'}
          $fontSize="extraSmall"
          $fontWeight="normal"
        >
          홈
        </Text>
      </IconWrapper>

      <IconSpace size={7} layoutWidth={layoutWidth} />

      <IconWrapper onClick={goToSeeTogether}>
        {navbarHighlights.seeTogether ? <FocusSeeTogether /> : <SeeTogether />}
        <Text
          color={navbarHighlights.seeTogether ? 'primary' : 'darkGray'}
          $fontSize="extraSmall"
          $fontWeight="normal"
        >
          모아보기
        </Text>
      </IconWrapper>

      <IconSpace size={7} layoutWidth={layoutWidth} />

      <IconWrapper onClick={onClickAddMapOrPin}>
        {navbarHighlights.addMapOrPin ? <FocusAddMapOrPin /> : <AddMapOrPin />}
        <Text
          color={navbarHighlights.addMapOrPin ? 'primary' : 'darkGray'}
          $fontSize="extraSmall"
          $fontWeight="normal"
        >
          추가하기
        </Text>
      </IconWrapper>

      <IconSpace size={7} layoutWidth={layoutWidth} />

      <IconWrapper onClick={goToFavorite}>
        {navbarHighlights.favorite ? <FocusFavorite /> : <Favorite />}
        <Text
          color={navbarHighlights.favorite ? 'primary' : 'darkGray'}
          $fontSize="extraSmall"
          $fontWeight="normal"
        >
          즐겨찾기
        </Text>
      </IconWrapper>

      <IconSpace size={7} layoutWidth={layoutWidth} />

      <IconWrapper onClick={goToProfile}>
        {navbarHighlights.profile ? <FocusProfile /> : <Profile />}
        <Text
          color={navbarHighlights.profile ? 'primary' : 'darkGray'}
          $fontSize="extraSmall"
          $fontWeight="normal"
        >
          내 정보
        </Text>
      </IconWrapper>

      <Modal
        position="center"
        width="232px"
        height="44px"
        dimmedColor="rgba(0,0,0,0)"
        top="calc(100vh - 100px)"
        left={layoutWidth === '100vw' ? '' : `${372 / 2}px`}
      >
        <Flex $justifyContent="center" width="100%">
          <RouteButton
            variant="primary"
            onClick={() => {
              routePage('/new-topic');
              closeModal();
            }}
          >
            지도 추가하기
          </RouteButton>
          <Space size={4} />
          <RouteButton
            variant="primary"
            onClick={() => {
              routePage('/');
              closeModal();
            }}
          >
            핀 추가하기
          </RouteButton>
        </Flex>
      </Modal>
    </Wrapper>
  );
};

const Wrapper = styled.nav<{ layoutWidth: '100vw' | '372px' }>`
  width: 100%;
  height: 64px;
  display: flex;
  justify-content: ${({ layoutWidth }) =>
    layoutWidth === '100vw' ? 'center' : 'space-around'};
  align-items: center;
`;

const IconWrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 44px;
  cursor: pointer;
`;

const IconSpace = styled(Space)<{ layoutWidth: '100vw' | '372px' }>`
  display: ${({ layoutWidth }) => (layoutWidth === '100vw' ? 'block' : 'none')};
`;

const RouteButton = styled(Button)`
  box-shadow: 2px 4px 4px rgba(0, 0, 0, 0.5);
`;

const ModalWrapper = styled(Flex)`
  width: 100%;
  height: 100%;
`;
export default Navbar;
