import Flex from '../common/Flex';
import Input from '../common/Input';
import Space from '../common/Space';
import Logo from './Logo';

type LayoutProps = {
  children: React.ReactNode;
};

const Layout = ({ children }: LayoutProps) => {
  return (
    <Flex
      $flexDirection="column"
      width="400px"
      height="100vh"
      $backgroundColor="white"
      padding={4}
    >
      <Logo />
      <Space size={5} />
      <Input />
      <Flex $flexDirection="column" overflow="scroll">
        {children}
      </Flex>
    </Flex>
  );
};

export default Layout;
