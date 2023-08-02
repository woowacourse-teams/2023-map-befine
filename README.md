# 2023-map-befine
## 소개 📝

💡 내 관심사로 🗺 만든 지도…? 괜찮을 지도!📍
<br><br>
괜찮을 지도는 “지도 기반 참여형 데이터 매핑 서비스”로서,<br>
당신의 관심사를 📍다양한 지도로 만들 수 있게 도와드리고 있어요!😉🌈

<br><br>
## 팀원 👨‍👨‍👧‍👧👩‍👦‍👦

|                                       Frontend                                        |                                        Frontend                                        |                                        Frontend                                        |
|:-------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------:|
| <img src="https://avatars.githubusercontent.com/u/89172499?v=4" width=130px alt="세인"> | <img src="https://avatars.githubusercontent.com/u/33995840?v=4" width=130px alt="아이크"> | <img src="https://avatars.githubusercontent.com/u/72205402?v=4" width=130px alt="패트릭"> |
|                          [세인](https://github.com/semnil5202)                          |                           [아이크](https://github.com/afds4567)                           |                           [패트릭](https://github.com/GC-Park)                            |

|                                        Backend                                         |                                        Backend                                         |                                         Backend                                         |                                        Backend                                        |
|:--------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------:|
| <img src="https://avatars.githubusercontent.com/u/97426362?v=4" width=130px alt="도이"/> | <img src="https://avatars.githubusercontent.com/u/89840550?v=4" width=130px alt="매튜"/> | <img src="https://avatars.githubusercontent.com/u/112045553?v=4" width=130px alt="준팍"/> | <img src="https://avatars.githubusercontent.com/u/50602742?v=4" width=130px alt="쥬니"> |
|                            [도이](https://github.com/yoondgu)                            |                           [매튜](https://github.com/kpeel5839)                           |                           [준팍](https://github.com/junpakPark)                           |                           [쥬니](https://github.com/cpot5620)                           | 

<br><br><br>
## 팀 문화 🏠

#### 기억보단 기록을
    - 결과물 없는 회의는 하지 않아요..
    - 클립보드에서 SSD로..!

#### 나 홀로 머지… 머지?
    - 나 혼자 밥을 먹고, 나 혼자 PR하고, 나 혼자 머지하고..
    - PR은 혼자, Merge는 다 함께!

#### 일주일에 한 번 식사와 회고를 한다.
    - 잡담이 경쟁력이다!
    - 내가 느낀 모든 것! 아쉬운 점도 감추지 않고 얘기해요.

#### 회의는 짧고 굵게!
    - 시간을 정해서 한 주제를 마무리 지어요.
    - 50분 회의에 10분 휴식

#### 10시 1분은 10시가 아니다.
    - 나의 1분은 모두의 7분입니다.
    - 돈으로 못사는 시간! 서로 존중합시다!

#### 협업은 일과시간 내에! (10 to 18)
    - 협업은 100M 달리기가 아닌, 마라톤
    - 혼자만의 시간 존중해주세요.

#### 침묵도 곧 부정이다!
    - 말하지 않으면 몰라요~
    - 누구인가? 누가 말하지 않았어?

#### 한 명이 말하면 여섯 명이 듣는다.
    - 더 좋은 아이디어를 알 기회를 놓치지는 않았나요?
    - 모두의 의견을 들을 때까지 잠시 더 기다려봐요.

#### 회의를 통해 결정된 것은 우리 모두의 책임이다.
    - 제발 현재를 살아…


#### 내가 뭐하는 지 팀이 알고, 팀이 뭐하는 지 내가 안다.
    - 당신의 Think, 우리와 Sync!
    - 데일리 미팅 (매일 10:00)
        - 컨디션 체크, 오늘 할 일, 공지사항 전파
    - 마무리 미팅 (매일 17:30)
        - 오늘까지의 진행 사항, 내일 할 일 전파
#### 질문을 두려워하지 마라
    - 이해하지 못했는데 알겠다고 대답하지 말아요.
    - 모르는 건 죄가 아니지만 모르는 데 아는 척 하는 건 죄입니다.


































- [ ] 해당 Topic 의 Creator 가 특정 멤버에게 Permission 을 주는 기능
    - [x] Controller, Service 코드 작성 (즉 기능 구현 완료)
    - [ ] Domain 테스트 작성
        - [x] MemberInfo 생성 테스트
        - [x] Member 생성 테스트
        - [x] MemberTopicPermission 에 createPermissionAssociatedWithTopicAndMember 잘 되는지 테스트 (연관관계 잘 추가되었는지)
        - [ ] Pin 의 정팩메를 사용할 때 Member 가 잘 추가 되는지
        - [ ] Topic 의 정팩메를 사용할 때 Member 가 잘 추가 되는지
    - [ ] Service 테스트 작성 (이 때에는 happy case 말고 예외 케이스 모두 검증하는 방식으로)
        - [ ] Admin 이 권한을 주는 경우
        - [ ] Creator 가 권한을 주는 경우
        - [ ] 그냥 일반 유저가 권한을 주려는 경우 (이 경우만 실패)
    - [ ] Controller 테스트 작성 (happy case 만)
        - [ ] Creator 가 권한을 주는 경우로 Controller Test 를 진행한다.
    - [ ] RestDocs 작성
        - [ ] Creator 가 권한을 주는 경우로 Controller Mocking Test 를 진행한다.

- [ ] 해당 Topic 의 Creator 가 특정 멤버의 Permission 을 삭제하는 기능
    - [x] Controller, Service 코드 작성 (즉 기능 구현 완료)
    - [ ] Domain 테스트 작성
    - [ ] Service 테스트 작성 (이 때에는 happy case 말고 예외 케이스 모두 검증하는 방식으로)
    - [ ] Controller 테스트 작성 (happy case 만)
    - [ ] RestDocs 작성

- [ ] 해당 Topic 에 Permission 을 가진 이들을 조회하는 기능 (로그인 유저에 한해서
    - [x] Controller, Service 코드 작성 (즉 기능 구현 완료)
    - [ ] Domain 테스트 작성
    - [ ] Service 테스트 작성 (이 때에는 happy case 말고 예외 케이스 모두 검증하는 방식으로)
    - [ ] Controller 테스트 작성 (happy case 만)
    - [ ] RestDocs 작성

- [ ] Member 를 Create 하는 기능
    - [x] Controller, Service 코드 작성 (즉 기능 구현 완료)
    - [ ] Domain 테스트 작성
    - [ ] Service 테스트 작성 (이 때에는 happy case 말고 예외 케이스 모두 검증하는 방식으로)
    - [ ] Controller 테스트 작성 (happy case 만)
    - [ ] RestDocs 작성

- [ ] Member 를 findAll 하는 기능
    - [x] Controller, Service 코드 작성 (즉 기능 구현 완료)
    - [ ] Domain 테스트 작성
    - [ ] Service 테스트 작성 (이 때에는 happy case 말고 예외 케이스 모두 검증하는 방식으로)
    - [ ] Controller 테스트 작성 (happy case 만)
    - [ ] RestDocs 작성

- [ ] Member ID 를 통해 조회하는 기능
    - [x] Controller, Service 코드 작성 (즉 기능 구현 완료)
    - [ ] Domain 테스트 작성
    - [ ] Service 테스트 작성 (이 때에는 happy case 말고 예외 케이스 모두 검증하는 방식으로)
    - [ ] Controller 테스트 작성 (happy case 만)
    - [ ] RestDocs 작성

- [ ] Member 가 본인이 만든 핀을 조회하는 기능
    - [x] Controller, Service 코드 작성 (즉 기능 구현 완료)
    - [ ] Domain 테스트 작성c
    - [ ] Service 테스트 작성 (이 때에는 happy case 말고 예외 케이스 모두 검증하는 방식으로)
    - [ ] Controller 테스트 작성 (happy case 만)
    - [ ] RestDocs 작성

- [ ]  Member 가 본인이 만든 토픽을 조회하는 기능
    - [x] Controller, Service 코드 작성 (즉 기능 구현 완료)
    - [ ] Domain 테스트 작성
    - [ ] Service 테스트 작성 (이 때에는 happy case 말고 예외 케이스 모두 검증하는 방식으로)
    - [ ] Controller 테스트 작성 (happy case 만)
    - [ ] RestDocs 작성



