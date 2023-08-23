import { styled } from 'styled-components';
import Flex from '../common/Flex';
import ProfileDefaultImage from '../../assets/profile_defaultImage.svg';
import ModifyMyInfoIcon from '../../assets/ModifyMyInfoIcon.svg';
import Box from '../common/Box';
import Text from '../common/Text';
import Space from '../common/Space';
import { useEffect, useState } from 'react';
import { MyInfoType } from '../../types/MyInfo';
import Button from '../common/Button';

interface UpdateMyInfoProps {
  isThereImg: boolean;
  myInfoNameAndEmail: MyInfoType;
  setIsModifyMyInfo: React.Dispatch<React.SetStateAction<boolean>>;
  setMyInfoNameAndEmail: React.Dispatch<React.SetStateAction<MyInfoType>>;
}

const UpdateMyInfo = ({
  isThereImg,
  myInfoNameAndEmail,
  setIsModifyMyInfo,
  setMyInfoNameAndEmail,
}: UpdateMyInfoProps) => {
  const onChangeMyInfoName = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.value.length >= 20) return;
    setMyInfoNameAndEmail({ ...myInfoNameAndEmail, name: e.target.value });
  };

  const onChangeMyInfoEmail = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.value.length >= 35) return;
    setMyInfoNameAndEmail({ ...myInfoNameAndEmail, email: e.target.value });
  };

  const onClickModifyButton = () => {
    setIsModifyMyInfo(false);
  };

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
        <ProfileDefaultImage />
      )}
      <Space size={7} />
      <Box>
        <MyInfoInput
          value={myInfoNameAndEmail.name}
          onChange={onChangeMyInfoName}
        />
        <Space size={0} />
        <MyInfoInput
          value={myInfoNameAndEmail.email}
          onChange={onChangeMyInfoEmail}
        />
      </Box>
      <Space size={3} />
      <Button variant="primary" onClick={onClickModifyButton}>
        확인
      </Button>
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

const MyInfoInput = styled.input`
  width: 150px;
  height: 35px;
  font-size: ${({ theme }) => theme.fontSize.default};
`;

export default UpdateMyInfo;
