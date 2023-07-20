import { rest } from 'msw';
import topics from './db/topics';
import detailTopic from './db/detailTopic';
import tempPins from './db/tempPins';

export const handlers = [
  // 포스트 목록
  rest.get('/pin/:id', (req, res, ctx) => {
    const pinId = req.params.id;
    return res(ctx.status(200), ctx.json(tempPins[pinId - 1]));
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
  rest.post('/pins', async (req, res, ctx) => {
    const { topicId, name, address, description } = await req.json();

    const newPin = {
      id: `${detailTopic[topicId - 1].pins.length + 1}`,
      name,
      description,
      address,
      latitude: '37',
      longitude: '127',
    };

    detailTopic[topicId - 1].pins.push(newPin);
    tempPins.push(newPin);

    if (!name) {
      return res(ctx.status(403), ctx.json(addData));
    }

    return res(
      ctx.status(201),
      ctx.set('Location', `/pin/${detailTopic[topicId - 1].pins.length}`),
    );
  }),

  rest.put('/pins/:id', async (req, res, ctx) => {
    const { id } = req.params;
    const { name, address, description } = await req.json();

    const pin = tempPins.find((pin) => pin.id === id);

    if (!pin) {
      return res(ctx.status(404), ctx.json({ message: 'Pin not found' }));
    }

    tempPins.forEach((pin) => {
      if (pin.id === id) {
        pin.name = name;
        pin.address = address;
        pin.description = description;
      }
      return pin;
    });

    detailTopic.forEach((topic) => {
      topic.pins.forEach((pin) => {
        if (pin.id === id) {
          pin.name = name;
          pin.address = address;
          pin.description = description;
        }
        return pin;
      });
      return topic;
    });

    return res(ctx.status(200), ctx.set('Location', `/pin/${id}`));
  }),
];
