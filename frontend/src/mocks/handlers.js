import { rest } from 'msw';
import topics from './db/topics';
import detailTopic from './db/detailTopic';

export const handlers = [
  // 포스트 목록
  rest.get('/pin/:id', (req, res, ctx) => {
    const pinId = req.params.id;

    return res(
      ctx.status(200),
      ctx.json({
        id: pinId,
        name: '오또상스시',
        address: '서울특별시 강남구 역삼동 123-456',
        description:
          '초밥을 파는 곳입니다. 점심 특선 있고 초밥 질이 괜찮습니다. 가격대도 다른 곳에 비해서 양호한 편이고 적당히 생각날 때 가면 좋을 것 같습니다.',
        latitude: '핀 위도',
        longitude: '핀 경도',
        updatedAt: '2023-07-12',
      }),
    );
  }),

  // 토픽 목록
  rest.get('/', (req, res, ctx) => {
    const data = topics;

    if (!data) {
      return res(ctx.status(403), ctx.json(data));
    }

    return res(
      ctx.set('Content-Type', 'application/json'),
      ctx.status(200),
      ctx.json(data),
    );
  }),

  // 토픽 디테일 보기
  rest.get('/topics/:id', (req, res, ctx) => {
    const topicId = Number(req.params.id);
    const data = detailTopic.filter((pin) => Number(pin.id) === topicId);

    if (!data) {
      return res(ctx.status(403), ctx.json(data));
    }

    return res(
      ctx.set('Content-Type', 'application/json'),
      ctx.status(200),
      ctx.json(data[0]),
    );
  }),

  // 토픽 생성
  rest.post('/topics/new', async (req, res, ctx) => {
    const { name, emoji, description } = await req.json();

    topics.push({
      id: `${topics.length + 1}`,
      emoji,
      name,
      description,
      pins: [],
      pinCount: 0,
      updatedAt: '2023-07-19',
    });

    detailTopic.push({
      id: `${detailTopic.length + 1}`,
      emoji,
      name,
      description,
      pins: [],
      pinCount: 0,
      updatedAt: '2023-07-19',
    });

    if (!name) {
      return res(ctx.status(403), ctx.json(addData));
    }

    return res(
      ctx.status(201),
      ctx.set('Location', `/topics/${topics.length}`),
    );
  }),

  // 핀 생성
  rest.post('/pins', (req, res, ctx) => {
    const newPin = {
      id: `${detailTopic[0].pins.length + 1}`,
      name: '찌개 맛있게 잘하는 집들',
      description: '선릉에서 찌개를 잘하는 집들이에요!',
      address: '서울특별시 선릉 테헤란로 127길 16',
      latitude: '핀 위도',
      longitude: '핀 경도',
    };

    detailTopic[0].pins.push(newPin);

    if (!newPin) {
      return res(ctx.status(403), ctx.json(addData));
    }

    return res(
      ctx.status(201),
      ctx.set('Location', `/pin/${detailTopic[0].pins.length}`),
    );
  }),
];
