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

-- -- Insert into Location table
-- INSERT INTO location (PARCEL_BASE_ADDRESS, ROAD_BASE_ADDRESS, LATITUDE, LONGITUDE, LEGAL_DONG_CODE, CREATED_AT,
--                       UPDATED_AT)
-- VALUES ('123 Fake St', '456 Fake Ave', 35.9078, 127.7669, '12345', NOW(), NOW()),
--        ('789 Fake St', '012 Fake Ave', 37.5665, 126.9780, '67890', NOW(), NOW());
--
-- -- Get the IDs of the inserted locations (replace these IDs in the subsequent queries if necessary)
-- SELECT id
-- FROM location;
--
-- -- Insert into Topic table
-- INSERT INTO topic (NAME, DESCRIPTION, IS_DELETED, CREATED_AT, UPDATED_AT)
-- VALUES ('Art', 'All about art and paintings', FALSE, NOW(), NOW()),
--        ('Science', 'All about science and technology', FALSE, NOW(), NOW());
--
-- -- Get the IDs of the inserted topics (replace these IDs in the subsequent queries if necessary)
-- SELECT id
-- FROM topic;
--
-- -- Insert into Pin table with references to the Location and Topic tables
-- -- Assuming the ids returned from above queries are 1 and 2 for Location, and 1 and 2 for Topic
-- INSERT INTO pin (NAME, DESCRIPTION, LOCATION_ID, TOPIC_ID, IS_DELETED, CREATED_AT, UPDATED_AT)
-- VALUES ('Pin1', 'Description1', 1, 1, FALSE, NOW(), NOW()),
--        ('Pin2', 'Description2', 2, 2, FALSE, NOW(), NOW());
