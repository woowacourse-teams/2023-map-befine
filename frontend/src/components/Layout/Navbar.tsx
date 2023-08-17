import { useContext } from 'react';
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
import { useParams } from 'react-router-dom';
import SeeTogetherCounter from '../SeeTogetherCounter';
import useKeyDown from '../../hooks/useKeyDown';

interface NavBarProps {
  $layoutWidth: '100vw' | '372px';
}

const Navbar = ({ $layoutWidth }: NavBarProps) => {
  const { routePage } = useNavigator();
  const { topicId } = useParams();
  const { openModal, closeModal } = useContext(ModalContext);
  const { navbarHighlights } = useContext(NavbarHighlightsContext);
  const { elementRef: firstElement, onElementKeyDown: firstKeyDown } =
    useKeyDown<HTMLDivElement>();
  const { elementRef: secondElement, onElementKeyDown: secondKeyDown } =
    useKeyDown<HTMLDivElement>();
  const { elementRef: thirdElement, onElementKeyDown: thirdKeyDown } =
    useKeyDown<HTMLDivElement>();
  const { elementRef: fourElement, onElementKeyDown: fourKeyDown } =
    useKeyDown<HTMLDivElement>();
  const { elementRef: FifthElement, onElementKeyDown: FifthKeyDown } =
    useKeyDown<HTMLDivElement>();

  const goToHome = () => {
    routePage('/');
  };

  const goToSeeTogether = () => {
    routePage('/see-together');
  };

  const onClickAddMapOrPin = () => {
    openModal('addMapOrPin');
  };

  const goToFavorite = () => {
    routePage('/favorite');
  };

  const goToProfile = () => {
    routePage('/my-page');
  };

  const goToNewTopic = () => {
    routePage('/new-topic');
    closeModal('addMapOrPin');
  };

  const goToNewPin = () => {
    routePage('/new-pin', topicId);
    closeModal('addMapOrPin');
  };

  return (
    <Wrapper $layoutWidth={$layoutWidth}>
      <IconWrapper
        onClick={goToHome}
        tabIndex={10}
        ref={firstElement}
        onKeyDown={firstKeyDown}
      >
        {navbarHighlights.home ? <FocusHome /> : <Home />}
        <Text
          color={navbarHighlights.home ? 'primary' : 'darkGray'}
          $fontSize="extraSmall"
          $fontWeight="normal"
        >
          홈
        </Text>
      </IconWrapper>

      <IconSpace size={7} $layoutWidth={$layoutWidth} />

      <IconWrapper
        onClick={goToSeeTogether}
        tabIndex={10}
        ref={secondElement}
        onKeyDown={secondKeyDown}
      >
        {navbarHighlights.seeTogether ? <FocusSeeTogether /> : <SeeTogether />}
        <Text
          color={navbarHighlights.seeTogether ? 'primary' : 'darkGray'}
          $fontSize="extraSmall"
          $fontWeight="normal"
        >
          모아보기
        </Text>
        <SeeTogetherCounter />
      </IconWrapper>

      <IconSpace size={7} $layoutWidth={$layoutWidth} />

      <IconWrapper
        onClick={onClickAddMapOrPin}
        tabIndex={10}
        ref={thirdElement}
        onKeyDown={thirdKeyDown}
      >
        {navbarHighlights.addMapOrPin ? <FocusAddMapOrPin /> : <AddMapOrPin />}
        <Text
          color={navbarHighlights.addMapOrPin ? 'primary' : 'darkGray'}
          $fontSize="extraSmall"
          $fontWeight="normal"
        >
          추가하기
        </Text>
      </IconWrapper>

      <IconSpace size={7} $layoutWidth={$layoutWidth} />

      <IconWrapper
        onClick={goToFavorite}
        tabIndex={11}
        ref={fourElement}
        onKeyDown={fourKeyDown}
      >
        {navbarHighlights.favorite ? <FocusFavorite /> : <Favorite />}
        <Text
          color={navbarHighlights.favorite ? 'primary' : 'darkGray'}
          $fontSize="extraSmall"
          $fontWeight="normal"
        >
          즐겨찾기
        </Text>
      </IconWrapper>

      <IconSpace size={7} $layoutWidth={$layoutWidth} />

      <IconWrapper
        onClick={goToProfile}
        tabIndex={11}
        ref={FifthElement}
        onKeyDown={FifthKeyDown}
      >
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
        modalKey="addMapOrPin"
        position="center"
        width="236px"
        height="48px"
        $dimmedColor="rgba(0,0,0,0)"
        top="calc(100vh - 100px)"
        left={$layoutWidth === '100vw' ? '' : `${372 / 2}px`}
      >
        <Flex $justifyContent="center" width="100%">
          <RouteButton variant="primary" onClick={goToNewTopic} tabIndex={10}>
            지도 추가하기
          </RouteButton>
          <Space size={4} />
          <RouteButton variant="primary" onClick={goToNewPin} tabIndex={10}>
            핀 추가하기
          </RouteButton>
        </Flex>
      </Modal>
    </Wrapper>
  );
};

const Wrapper = styled.nav<{ $layoutWidth: '100vw' | '372px' }>`
  width: 100%;
  height: 64px;
  display: flex;
  justify-content: ${({ $layoutWidth }) =>
    $layoutWidth === '100vw' ? 'center' : 'space-around'};
  align-items: center;
`;

const IconWrapper = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 52px;
  cursor: pointer;
`;

const IconSpace = styled(Space)<{ $layoutWidth: '100vw' | '372px' }>`
  display: ${({ $layoutWidth }) =>
    $layoutWidth === '100vw' ? 'block' : 'none'};
`;

const RouteButton = styled(Button)`
  box-shadow: 2px 4px 4px rgba(0, 0, 0, 0.5);
`;

const ModalWrapper = styled(Flex)`
  width: 100%;
  height: 100%;
`;
export default Navbar;
