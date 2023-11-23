import LogoImage from '../../assets/logo.svg';
import useKeyDown from '../../hooks/useKeyDown';
import useNavigator from '../../hooks/useNavigator';
import Box from '../common/Box';

function Logo() {
  const { routePage } = useNavigator();
  const { elementRef, onElementKeyDown } = useKeyDown<HTMLDivElement>();

  const goToHome = () => {
    routePage('/');
  };

  return (
    <Box
      cursor="pointer"
      onKeyDown={onElementKeyDown}
      ref={elementRef}
      onClick={goToHome}
      tabIndex={0}
      aria-label="괜찮을지도 로고 및 홈으로 이동하기"
    >
      <LogoImage />
    </Box>
  );
}

export default Logo;
