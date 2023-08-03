import LogoImage from '../../assets/logo.svg';
import useNavigator from '../../hooks/useNavigator';
import Box from '../common/Box';

const Logo = () => {
  const { routePage } = useNavigator();

  const goToHome = () => {
    routePage('/');
  };

  return (
    <Box cursor="pointer">
      <LogoImage onClick={goToHome} aria-label="괜찮을지도 로고" tabIndex={0}/>
    </Box>
  );
};

export default Logo;
