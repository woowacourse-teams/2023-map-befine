import { useContext } from 'react';
import { styled } from 'styled-components';

import AddMapOrPin from '../../assets/nav_addMapOrPin.svg';
import FocusAddMapOrPin from '../../assets/nav_addMapOrPin_focus.svg';
import Favorite from '../../assets/nav_favorite.svg';
import FocusFavorite from '../../assets/nav_favorite_focus.svg';
import Home from '../../assets/nav_home.svg';
import FocusHome from '../../assets/nav_home_focus.svg';
import Profile from '../../assets/nav_profile.svg';
import FocusProfile from '../../assets/nav_profile_focus.svg';
import SeeTogether from '../../assets/nav_seeTogether.svg';
import FocusSeeTogether from '../../assets/nav_seeTogether_focus.svg';
import {
  NavbarHighlightKeys,
  NavbarHighlightsContext,
} from '../../context/NavbarHighlightsContext';
import useNavigator from '../../hooks/useNavigator';
import Button from '../common/Button';
import Flex from '../common/Flex';
import Space from '../common/Space';
import Modal from '../Modal';
import NavbarItem from './NavbarItem';

interface NavBarProps {
  $layoutWidth: '100vw' | '372px';
}

interface NavbarItemProps {
  key: NavbarHighlightKeys;
  label: string;
  icon: React.FunctionComponent;
  focusIcon: React.FunctionComponent;
}

const NAV_ITEMS: NavbarItemProps[] = [
  { key: 'home', label: '홈', icon: Home, focusIcon: FocusHome },
  {
    key: 'seeTogether',
    label: '모아보기',
    icon: SeeTogether,
    focusIcon: FocusSeeTogether,
  },
  {
    key: 'addMapOrPin',
    label: '추가하기',
    icon: AddMapOrPin,
    focusIcon: FocusAddMapOrPin,
  },
  {
    key: 'favorite',
    label: '즐겨찾기',
    icon: Favorite,
    focusIcon: FocusFavorite,
  },
  {
    key: 'profile',
    label: '내 정보',
    icon: Profile,
    focusIcon: FocusProfile,
  },
];

function Navbar({ $layoutWidth }: NavBarProps) {
  const { routingHandlers } = useNavigator();
  const { navbarHighlights } = useContext(NavbarHighlightsContext);
  return (
    <>
      <Wrapper $layoutWidth={$layoutWidth}>
        {NAV_ITEMS.map((item) => (
          <NavbarItem
            key={item.key}
            label={item.label}
            icon={item.icon}
            focusIcon={item.focusIcon}
            isHighlighted={navbarHighlights[item.key]}
            onClick={() => routingHandlers[item.key]()}
            $layoutWidth={$layoutWidth}
          />
        ))}
      </Wrapper>

      <Modal
        modalKey="addMapOrPin"
        position="center"
        width="252px"
        height="64px"
        $dimmedColor="rgba(0,0,0,0)"
        top="calc(var(--vh, 1vh) * 100 - 100px)"
        left={$layoutWidth === '100vw' ? '' : `${372 / 2}px`}
      >
        <Flex $justifyContent="center" width="100%">
          <RouteButton
            variant="primary"
            onClick={routingHandlers.newTopic}
            tabIndex={10}
          >
            지도 추가하기
          </RouteButton>
          <Space size={4} />
          <RouteButton
            variant="primary"
            onClick={routingHandlers.newPin}
            tabIndex={10}
          >
            핀 추가하기
          </RouteButton>
        </Flex>
      </Modal>
    </>
  );
}

const Wrapper = styled.nav<{
  $layoutWidth: '100vw' | '372px';
}>`
  width: 100%;
  min-height: 56px;
  display: flex;
  justify-content: ${({ $layoutWidth }) =>
    $layoutWidth === '100vw' ? 'center' : 'space-around'};
  align-items: center;
  background-color: ${({ theme }) => theme.color.white};
  z-index: 2;
  box-shadow: 0 -1px 8px rgba(0, 0, 0, 0.3);

  @media (max-width: 1076px) {
    justify-content: space-around;
    position: fixed;
    bottom: 0;
  }
`;

const RouteButton = styled(Button)`
  box-shadow: 2px 4px 4px rgba(0, 0, 0, 0.5);
`;

export default Navbar;
