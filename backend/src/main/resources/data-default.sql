INSERT INTO member (nick_name, email, image_url, role,
                    oauth_server_id, oauth_server_type,
                    created_at, updated_at)
VALUES ('dummyMember', 'dummy@gmail.com', 'https://map-befine-official.github.io/favicon.png', 'USER',
        1L, 'KAKAO',
        now(), now());

INSERT INTO topic (name, image_url, description,
                   permission_type, publicity,
                   member_id,
                   created_at, updated_at)
VALUES ('dummyTopic', 'https://map-befine-official.github.io/favicon.png', 'description',
        'ALL_MEMBERS', 'PUBLIC',
        1L,
        now(), now())
;
