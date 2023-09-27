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
import { DEFAULT_PROD_URL } from '../../constants';
import Setting from '../../assets/setting.svg';

const user = JSON.parse(localStorage.getItem('user') || '{}');
const accessToken = localStorage.getItem('userToken');

const MyInfo = () => {
  const { showToast } = useToast();

  const [isModifyMyInfo, setIsModifyMyInfo] = useState<boolean>(false);
  const [myInfo, setMyInfo] = useState<ProfileProps>({
    name: user.nickName,
    email: user.email,
    image: user.imageUrl,
  });

  const onClickSetting = () => {
    setIsModifyMyInfo(true);
  };

  const onClickLogout = async (e: React.MouseEvent<HTMLButtonElement>) => {
    try {
      fetch(`${DEFAULT_PROD_URL}/logout`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${accessToken}`,
        },
        body: JSON.stringify({
          accessToken: accessToken,
        }),
      });

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
        myInfo={myInfo}
        setIsModifyMyInfo={setIsModifyMyInfo}
        setMyInfo={setMyInfo}
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
      <SettingContainer onClick={onClickSetting}>
        <Setting />
      </SettingContainer>
      <MyInfoImg src={user.imageUrl} />
      <Space size={5} />
      <Box>
        <Flex $justifyContent="space-between" $alignItems="center">
          <Text color="black" $fontSize="medium" $fontWeight="bold">
            {user.nickName}
          </Text>
          <Button variant="custom" onClick={onClickLogout}>
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

const SettingContainer = styled.div`
  position: absolute;
  top: 10px;
  right: 10px;
  cursor: pointer;
`;

export default MyInfo;
