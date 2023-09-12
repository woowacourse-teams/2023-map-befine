import { styled } from 'styled-components';
import Box from '../components/common/Box';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import MyInfo from '../components/MyInfo';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import { FULLSCREEN } from '../constants';
import TopicCardContainerSkeleton from '../components/Skeletons/TopicListSkeleton';
import { Suspense, lazy } from 'react';
import Text from '../components/common/Text';
import useNavigator from '../hooks/useNavigator';
import { setFullScreenResponsive } from '../constants/responsive';

const TopicCardList = lazy(() => import('../components/TopicCardList'));

const Profile = () => {
  const { routePage } = useNavigator();
  useSetLayoutWidth(FULLSCREEN);
  useSetNavbarHighlight('profile');

  const goToNewTopic = () => {
    routePage('/new-topic');
  };

  return (
    <Wrapper>
      <MyInfoWrapper $justifyContent="center" $alignItems="center">
        <MyInfo />
      </MyInfoWrapper>

      <Space size={6} />

      <Flex $justifyContent="space-between" $alignItems="flex-end">
        <Box>
          <Text
            color="black"
            $fontSize="extraLarge"
            $fontWeight="bold"
            tabIndex={0}
          >
            나의 지도
          </Text>
          <Space size={0} />
          <Text
            color="gray"
            $fontSize="default"
            $fontWeight="normal"
            tabIndex={1}
          >
            내가 만든 지도를 확인해보세요.
          </Text>
        </Box>
      </Flex>

      <Space size={6} />

      <Suspense fallback={<TopicCardContainerSkeleton />}>
        <TopicCardList
          url="/members/my/topics"
          errorMessage="로그인 후 이용해주세요."
          commentWhenEmpty="추가하기 버튼을 눌러 지도를 추가해보세요."
          pageCommentWhenEmpty="지도 만들러 가기"
          routePage={goToNewTopic}
        />
      </Suspense>
    </Wrapper>
  );
};

const Wrapper = styled(Box)`
  width: 1036px;
  margin: 0 auto;

  ${setFullScreenResponsive()}
`;

const MyInfoWrapper = styled(Flex)`
  margin-top: ${({ theme }) => theme.spacing[6]};
`;
export default Profile;
