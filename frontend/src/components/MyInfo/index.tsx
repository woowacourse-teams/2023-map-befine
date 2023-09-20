import { styled } from 'styled-components';
import Flex from '../common/Flex';
import Box from '../common/Box';
import Text from '../common/Text';
import Space from '../common/Space';
import { useState } from 'react';
import { ProfileProps } from '../../types/Profile';
import UpdateMyInfo from './UpdateMyInfo';
import Button from '../common/Button';
import useToast from '../../hooks/useToast';
import { postApi } from '../../apis/postApi';

const user = JSON.parse(localStorage.getItem('user') || '{}');
const accessToken = localStorage.getItem('userToken');

const MyInfo = () => {
  const { showToast } = useToast();

  const [isThereImg, setIsThereImg] = useState<boolean>(true);
  const [isModifyMyInfo, setIsModifyMyInfo] = useState<boolean>(false);
  const [myInfoNameAndEmail, setMyInfoNameAndEmail] = useState<ProfileProps>({
    name: user.nickName,
    email: user.email,
  });

  const onClickLogout = async (e: React.MouseEvent<HTMLButtonElement>) => {
    try {
      await postApi(
        `/logout`,
        {
          accessToken: accessToken,
        },
        'x-www-form-urlencoded',
      );

      localStorage.removeItem('user');
      localStorage.removeItem('userToken');
      window.location.href = '/';
      showToast('info', '로그아웃 되었습니다.');
    } catch {
      showToast('error', '로그아웃에 실패했습니다');
    }
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
      <MyInfoImg src={user.imageUrl} />
      <Space size={5} />
      <Box>
        <Flex $justifyContent="space-between" $alignItems="center">
          <Text color="black" $fontSize="medium" $fontWeight="bold">
            {user.nickName}
          </Text>
          <Button variant="primary" onClick={onClickLogout}>
            로그아웃
          </Button>
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
