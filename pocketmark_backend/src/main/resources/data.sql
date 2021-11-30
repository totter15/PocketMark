-- -- call next value for hibernate_sequence;
insert into folder (`id`, `name`, `created_at`, `updated_at`,`deleted`,`TOTAL_VISIT_COUNT`) values (1, 'Home', now(), now(),false,0);
-- -- call next value for hibernate_sequence;

insert into user( `id`, `created_at`,`updated_at`,`deleted`,`user_id`,`user_pw`,`email`) values (2,now(),now(),false,'sim2280','1234','sim2626@naver.com');



