import Flex from '../Flex';
import Input from '../Input';

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
      <Input />
      <Flex $flexDirection="column" overflow="scroll">
        {children}
      </Flex>
    </Flex>
  );
};

export default Layout;
