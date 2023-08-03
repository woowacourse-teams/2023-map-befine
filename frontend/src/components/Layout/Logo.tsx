import LogoImage from '../../assets/logo.svg';
import useNavigator from '../../hooks/useNavigator';
import Box from '../common/Box';
import { KeyboardEvent, useRef } from 'react';

const Logo = () => {
  const { routePage } = useNavigator();

  const divRef = useRef<HTMLDivElement | null>(null);

  const goToHome = () => {
    routePage('/');
  };

  const onDivKeyDown = (e: KeyboardEvent<HTMLDivElement>) => {
    if (e.keyCode === 13) {
      e.preventDefault();
      divRef.current?.click();
    }
  };

  return (
    <Box
      cursor="pointer"
      onKeyDown={onDivKeyDown}
      ref={divRef}
      onClick={goToHome}
      aria-label="괜찮을지도 로고 및 홈으로 이동 버튼"
      tabIndex={0}
    >
      <LogoImage />
    </Box>
  );
};

export default Logo;
