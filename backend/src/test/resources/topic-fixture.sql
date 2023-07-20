-- Location
insert into LOCATION (parcel_base_address, road_base_address, latitude, longitude, legal_dong_code)
values ('지번 주소1', '도로명 주소1', 0, 0, '법정동1');
insert into LOCATION (parcel_base_address, road_base_address, latitude, longitude, legal_dong_code)
values ('지번 주소2', '도로명 주소2', 0, 0, '법정동2');


-- Topic
insert into TOPIC (name, description, is_deleted)
values ('준팍의 또간집', '준팍이 두번 이상 간 집', false);

insert
into TOPIC (name, description, is_deleted)
values ('도이의 또간집', '도이가 두번 이상 간 집', false);


-- Pin
insert into PIN (name, description, location_id, topic_id)
VALUES ('핀1', '첫번째 핀', 1L, 1L);

insert into PIN (name, description, location_id, topic_id)
VALUES ('핀', '두번째 핀', 2L, 2L);
