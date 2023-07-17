import Flex from '../Flex';
import Input from '../Input';

type LayoutProps = {
  children: React.ReactNode;
};

const Layout = ({ children }: LayoutProps) => {
  return (
    <Flex
      flexDirection="column"
      width="400px"
      backgroundColor="lightGray"
      padding={4}
    >
      <Input />
      <Flex flexDirection="column">{children}</Flex>
    </Flex>
  );
};

export default Layout;
