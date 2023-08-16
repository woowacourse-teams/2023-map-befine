import { styled } from 'styled-components';
import Flex from '../common/Flex';
import Box from '../common/Box';
import Text from '../common/Text';
import Space from '../common/Space';
import { useState } from 'react';
import { MyInfoType } from '../../types/MyInfo';
import UpdateMyInfo from './UpdateMyInfo';

const MyInfo = () => {
  const [isThereImg, setisThereImg] = useState<boolean>(true);
  const [isModifyMyInfo, setIsModifyMyInfo] = useState<boolean>(false);
  const [myInfoNameAndEmail, setMyInfoNameAndEmail] = useState<MyInfoType>({
    name: 'Patrick',
    email: 'qkrtk9230@naver.com',
  });
  const user = JSON.parse(localStorage.getItem('user') || '');

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
      <Space size={7} />
      <Box>
        <Text color="black" $fontSize="default" $fontWeight="normal">
          {myInfoNameAndEmail.name}
        </Text>
        <Text color="black" $fontSize="small" $fontWeight="normal">
          {myInfoNameAndEmail.email}
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
