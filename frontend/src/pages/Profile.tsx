import { lazy, Suspense } from 'react';
import { styled } from 'styled-components';

import Box from '../components/common/Box';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import MediaSpace from '../components/common/Space/MediaSpace';
import MediaText from '../components/common/Text/MediaText';
import MyInfo from '../components/MyInfo';
import TopicListSkeleton from '../components/Skeletons/TopicListSkeleton';
import { ARIA_FOCUS, FULLSCREEN } from '../constants';
import useNavigator from '../hooks/useNavigator';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';

const TopicCardList = lazy(() => import('../components/TopicCardList'));

function Profile() {
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
          <MediaText
            as="h2"
            color="black"
            $fontSize="extraLarge"
            $fontWeight="bold"
            tabIndex={ARIA_FOCUS}
            aria-label="나의 지도 영역입니다. 내가 만든 지도들을 확인할 수 있습니다."
          >
            나의 지도
          </MediaText>
          <Space size={0} />
          <MediaText color="gray" $fontSize="default" $fontWeight="normal">
            내가 만든 지도를 확인해보세요.
          </MediaText>
        </Box>
      </Flex>

      <MediaSpace size={6} />

      <Suspense fallback={<TopicListSkeleton />}>
        <TopicCardList
          url="/members/my/topics"
          errorMessage="로그인 후 이용해주세요."
          commentWhenEmpty="추가하기 버튼을 눌러 지도를 추가해보세요."
          pageCommentWhenEmpty="지도 만들러 가기"
          routePage={goToNewTopic}
        />
      </Suspense>

      <Space size={8} />
    </Wrapper>
  );
}

const Wrapper = styled(Box)`
  width: 1140px;
  margin: 0 auto;
  position: relative;

  @media (max-width: 1180px) {
    width: 100%;
  }
`;

const MyInfoWrapper = styled(Flex)`
  margin-top: ${({ theme }) => theme.spacing[6]};
`;
export default Profile;
