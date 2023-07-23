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
      <LogoImage onClick={goToHome} />
    </Box>
  );
};

export default Logo;
