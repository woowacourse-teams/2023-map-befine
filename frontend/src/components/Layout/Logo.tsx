import { useNavigate } from 'react-router-dom';
import LogoImage from '../../assets/logo.svg';
import Box from '../common/Box';

const Logo = () => {
  const navigator = useNavigate();

  const goToHome = () => {
    navigator('/');
  };

  return (
    <Box cursor="pointer">
      <LogoImage onClick={goToHome} />
    </Box>
  );
};

export default Logo;
