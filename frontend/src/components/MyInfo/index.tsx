import { styled } from 'styled-components';
import Flex from '../common/Flex';
import Box from '../common/Box';
import Text from '../common/Text';
import Space from '../common/Space';
import { useState } from 'react';
import { ProfileProps } from '../../types/Profile';
import UpdateMyInfo from './UpdateMyInfo';
import Button from '../common/Button';

const user = JSON.parse(localStorage.getItem('user') || '{}');

const MyInfo = () => {
  const [isThereImg, setIsThereImg] = useState<boolean>(true);
  const [isModifyMyInfo, setIsModifyMyInfo] = useState<boolean>(false);
  const [myInfoNameAndEmail, setMyInfoNameAndEmail] = useState<ProfileProps>({
    name: user.nickName,
    email: user.email,
  });

  if (isModifyMyInfo) {
    return (
      <UpdateMyInfo
        isThereImg={isThereImg}
        myInfoNameAndEmail={myInfoNameAndEmail}
        setIsModifyMyInfo={setIsModifyMyInfo}
        setMyInfoNameAndEmail={setMyInfoNameAndEmail}
      />
    );
  }

  return (
    <MyInfoContainer
      width="440px"
      height="140px"
      $borderRadius="medium"
      $justifyContent="center"
      $alignItems="center"
    >
      <MyInfoImg src={user.imageUrl} />
      <Space size={5} />
      <Box>
        <Flex $justifyContent="space-between" $alignItems="center">
          <Text color="black" $fontSize="medium" $fontWeight="bold">
            {user.nickName}
          </Text>
          <Button variant="primary">로그아웃</Button>
        </Flex>
        <Text color="black" $fontSize="small" $fontWeight="normal">
          {user.email}
        </Text>
      </Box>
    </MyInfoContainer>
  );
};

const MyInfoContainer = styled(Flex)`
  position: relative;
  border: 1px solid ${({ theme }) => theme.color.lightGray};
`;

const MyInfoImg = styled.img`
  width: 80px;
  height: 80px;

  border-radius: 50%;
`;

export default MyInfo;
