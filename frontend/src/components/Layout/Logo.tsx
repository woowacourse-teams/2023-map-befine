import LogoImage from '../../assets/logo.svg';
import useNavigator from '../../hooks/useNavigator';
import Box from '../common/Box';
import useKeyDown from '../../hooks/useKeyDown';

const Logo = () => {
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
      aria-label="괜찮을지도 로고 및 홈으로 이동 버튼"
      tabIndex={0}
    >
      <LogoImage />
    </Box>
  );
};

export default Logo;
