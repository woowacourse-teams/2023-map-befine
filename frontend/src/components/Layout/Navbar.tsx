import Flex from '../common/Flex';
import Home from '../../assets/Home.svg';
import All from '../../assets/All.svg';
import AddButton from '../../assets/addButton.svg';
import Favorite from '../../assets/Favorite.svg';
import Keep from '../../assets/Keep.svg';
import useNavigator from '../../hooks/useNavigator';
import { useState } from 'react';
import Button from '../common/Button';
import Space from '../common/Space';
import Tooltip from '../common/Tooltip';

const Navbar = () => {
  const { routePage } = useNavigator();
  const [showButton, setShowButton] = useState(false);

  return (
    <Flex
      width="400px"
      height="56px"
      padding="20px"
      $backgroundColor="black"
      position="absolute"
      $alignItems="center"
      $justifyContent="space-between"
      bottom="0"
    >
      <Home onClick={() => routePage('/')} />
      <All onClick={() => routePage('/all')} />
      <Tooltip content="나만의 지도를 추가해보세요" position="top">
        <AddButton
          onClick={() => {
            setShowButton((prev) => !prev);
          }}
        />
        {showButton && (
          <Flex
            position="absolute"
            bottom="60px"
            $justifyContent="center"
            padding="20px"
            left="0"
            width="100%"
          >
            <Button
              variant="primary"
              onClick={() => {
                routePage('/new-topic');
                setShowButton(false);
              }}
            >
              지도 추가하기
            </Button>
            <Space size={4} />
            <Button variant="primary" onClick={() => routePage('/')}>
              핀 추가하기
            </Button>
          </Flex>
        )}
      </Tooltip>
      <Favorite onClick={() => routePage('/favorite')} />
      <Keep onClick={() => routePage('/keep')} />
    </Flex>
  );
};

export default Navbar;
