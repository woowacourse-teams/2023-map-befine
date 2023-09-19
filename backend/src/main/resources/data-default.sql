INSERT INTO member (nick_name, email, image_url, role, status,
                    oauth_server_id, oauth_server_type,
                    created_at, updated_at)
VALUES ('dummyMember', 'dummy@gmail.com', 'https://map-befine-official.github.io/favicon.png', 'USER', 'NORMAL',
        1L, 'KAKAO',
        now(), now());

INSERT INTO topic (name, image_url, description,가
                   permission_type, publicity,
                   member_id,
                   created_at, updated_at, last_pin_updated_at)
VALUES ('dummyTopic', 'https://map-befine-official.github.io/favicon.png', 'description',
        'ALL_MEMBERS', 'PUBLIC',
        1L,
        now(), now(), now())
;
