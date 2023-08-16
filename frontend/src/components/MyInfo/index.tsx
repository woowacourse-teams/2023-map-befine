import { styled } from 'styled-components';
import Flex from '../common/Flex';
import InfoDefalutImg from '../../assets/InfoDefalutImg.svg';
import ModifyMyInfoIcon from '../../assets/ModifyMyInfoIcon.svg';
import Box from '../common/Box';
import Text from '../common/Text';
import Space from '../common/Space';
import { useEffect, useState } from 'react';
import UpdateMyInfo from './updateMyInfo';
import { MyInfoType } from '../../types/MyInfo';

const MyInfo = () => {
  const [isThereImg, setisThereImg] = useState<boolean>(true);
  const [isModifyMyInfo, setIsModifyMyInfo] = useState<boolean>(false);
  const [myInfoNameAndEmail, setMyInfoNameAndEmail] = useState<MyInfoType>({
    name: 'Patrick',
    email: 'qkrtk9230@naver.com',
  });

  //   useEffect(()=>{
  //     setMyInfoName()
  //   }, [])

  const onModifyInfo = () => {
    setIsModifyMyInfo(true);
  };

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
      {isThereImg ? (
        <MyInfoImg src="https://images.unsplash.com/photo-1480429370139-e0132c086e2a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=988&q=80" />
      ) : (
        <InfoDefalutImg />
      )}
      <Space size={7} />
      <Box>
        <Text color="black" $fontSize="default" $fontWeight="normal">
          {myInfoNameAndEmail.name}
        </Text>
        <Text color="black" $fontSize="small" $fontWeight="normal">
          {myInfoNameAndEmail.email}
        </Text>
      </Box>
      <MyInfoModifyIcon>
        <ModifyMyInfoIcon onClick={onModifyInfo} />
      </MyInfoModifyIcon>
    </MyInfoContainer>
  );
};

const MyInfoContainer = styled(Flex)`
  border: 1px solid ${({ theme }) => theme.color.lightGray};
`;

const MyInfoImg = styled.img`
  width: 80px;
  height: 80px;

  border-radius: 50%;
`;

const MyInfoModifyIcon = styled(Box)`
  position: absolute;
  right: 32px;
  top: 32px;
`;

export default MyInfo;
