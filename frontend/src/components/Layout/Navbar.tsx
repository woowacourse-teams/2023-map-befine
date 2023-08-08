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
      <Home stroke="red" onClick={() => routePage('/')} />
      <All fill="red" stroke="red" onClick={() => routePage('/all')} />
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
        // <AddButton style={{ position: 'absolute', bottom: '60' }} />
      )}
      <Favorite fill="red" onClick={() => routePage('/favorite')} />
      <Keep fill="red" onClick={() => routePage('/keep')} />
    </Flex>
  );
};

export default Navbar;
