import { rest } from 'msw';
import topics from './db/topics';
import newestTopics from './db/newestTopics';
import bestTopics from './db/bestTopics';
import detailTopic from './db/detailTopic';
import tempPins from './db/tempPins';
import resLogin from './db/resLogin';
import bookmarks from './db/bookmarks';
import myTopics from './db/myTopics';
import atlas from './db/atlas';

export const handlers = [
  // 포스트 목록
  rest.get('/pins/:id', (req, res, ctx) => {
    const pinId = req.params.id;
    return res(ctx.status(200), ctx.json(tempPins[pinId - 1]));
  }),

  // 토픽 목록
  rest.get('/topics', (req, res, ctx) => {
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

  // 인기 급상승 토픽 목록
  rest.get('/topics/bests', (req, res, ctx) => {
    const data = bestTopics;

    if (!data) {
      return res(ctx.status(403), ctx.json(data));
    }

    return res(
      ctx.set('Content-Type', 'application/json'),
      ctx.status(200),
      ctx.json(data),
    );
  }),

  // 최신 토픽 목록
  rest.get('/topics/newest', (req, res, ctx) => {
    const data = newestTopics;

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
  rest.get('/topics/ids?ids=:id', (req, res, ctx) => {
    let data = [];
    if (req.url.searchParams.get('ids').split(',').length > 1) {
      const topicId = req.url.searchParams.get('ids').split(',');
      topicId.forEach((id) => {
        detailTopic.forEach((topic) => {
          if (Number(topic.id) === Number(id)) {
            data.push(topic);
          }
        });
      });
    } else {
      const topicId = Number(req.url.searchParams.get('ids'));
      data = detailTopic.filter((topic) => Number(topic.id) === topicId);
    }

    if (!data) {
      return res(ctx.status(403), ctx.json(data));
    }

    return res(
      ctx.set('Content-Type', 'application/json'),
      ctx.status(200),
      ctx.json(data),
    );
  }),

  // login
  rest.get('/login', (req, res, ctx) => {
    const data = resLogin;

    return res(
      ctx.set('Content-Type', 'application/json'),
      ctx.status(200),
      ctx.json(data),
    );
  }),

  // bookmarks
  rest.get('/members/my/bookmarks', (req, res, ctx) => {
    const data = bookmarks;

    return res(
      ctx.set('Content-Type', 'application/json'),
      ctx.status(200),
      ctx.json(data),
    );
  }),

  // 나의 지도 목록
  rest.get('/members/my/topics', (req, res, ctx) => {
    const data = myTopics;

    return res(
      ctx.set('Content-Type', 'application/json'),
      ctx.status(200),
      ctx.json(data),
    );
  }),

  rest.get('/members/my/atlas', (req, res, ctx) => {
    const data = atlas;

    return res(
      ctx.set('Content-Type', 'application/json'),
      ctx.status(200),
      ctx.json(data),
    );
  }),

  // 토픽 생성
  rest.post('/topics/new', async (req, res, ctx) => {
    const { name, image, description, pins, publicity, permissionType } =
      await req.json();

    topics.push({
      id: `${topics.length + 1}`,
      image,
      name,
      creator: '패트릭',
      isInAtlas: false,
      isBookmarked: false,
      bookmarkCount: 5,
      pinCount: 0,
      updatedAt: '2023-08-17T20:45:00.123284785',
    });

    newestTopics.push({
      id: `${newestTopics.length + 1}`,
      image,
      name,
      creator: '패트릭',
      isInAtlas: false,
      isBookmarked: false,
      bookmarkCount: 5,
      pinCount: 0,
      updatedAt: '2023-08-17T20:45:00.123284785',
    });

    bestTopics.push({
      id: `${bestTopics.length + 1}`,
      image,
      name,
      creator: '패트릭',
      isInAtlas: false,
      isBookmarked: false,
      bookmarkCount: 5,
      pinCount: 0,
      updatedAt: '2023-08-17T20:45:00.123284785',
    });

    detailTopic.push({
      id: `${detailTopic.length + 1}`,
      image,
      name,
      description,
      creator: '패트릭',
      isInAtlas: false,
      pins: pins,
      isBookmarked: false,
      bookmarkCount: 5,
      pinCount: 0,
      updatedAt: '2023-08-17T20:45:00.123284785',
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
    const { topicId, name, address, description, latitude, longitude } =
      await req.json();

    const newPin = {
      id: `${detailTopic[topicId - 1].pins.length + 1}`,
      name,
      description,
      address,
      latitude: latitude,
      longitude: longitude,
      legalDongCode: '',
      images: [],
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

  // 즐겨찾기 추가
  rest.post('/bookmarks/topics?id=:id', async (req, res, ctx) => {
    const id = req.url.searchParams.get('id');

    const bookmarkTopic = [];
    topics.forEach((topic) => {
      if (topic.id === Number(id)) {
        bookmarkTopic.push(topic);
      }
    });

    bookmarks.push(bookmarkTopic[0]);

    return res(
      ctx.status(201),
      ctx.set('Location', `/bookmarks/topics/${id}}`),
    );
  }),

  // 모아보기 추가
  rest.post('/atlas/topics?id=:id', async (req, res, ctx) => {
    const id = req.url.searchParams.get('id');

    const atlasTopic = [];
    topics.forEach((topic) => {
      if (topic.id === Number(id)) {
        atlasTopic.push(topic);
      }
    });

    atlas.push(atlasTopic[0]);

    return res(ctx.status(201));
  }),

  // pin 변경
  rest.put('/pins/:id', async (req, res, ctx) => {
    const { id } = req.params;
    const { name, image, description } = await req.json();

    const pin = tempPins.find((pin) => pin.id === id);

    if (!pin) {
      return res(ctx.status(404), ctx.json({ message: 'Pin not found' }));
    }

    tempPins.forEach((pin) => {
      if (pin.id === id) {
        pin.name = name;
        pin.image = image;
        pin.description = description;
      }
      return pin;
    });

    detailTopic.forEach((topic) => {
      topic.pins.forEach((pin) => {
        if (pin.id === id) {
          pin.name = name;
          pin.image = image;
          pin.description = description;
        }
        return pin;
      });
      return topic;
    });

    return res(ctx.status(200), ctx.set('Location', `/pin/${id}`));
  }),
];
