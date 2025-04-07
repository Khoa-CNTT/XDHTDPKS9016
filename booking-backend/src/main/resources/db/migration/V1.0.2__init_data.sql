USE booking_management;

INSERT INTO account(username,password)
VALUES
    ('admin','$2a$12$ayih9HWcXCFc/0uhR4fIX.coCWPdnrYE07HJfOnawKfRAHcpYDOQi'),
    ('user','$2a$12$h1jrIrcuByJFrt4OnLWS5./5R6sfRquG1FqZh2MEASTGtKrsLrNqC')
;

INSERT INTO `role`(role_name)
VALUES
    ('ADMIN'),
    ('USER')
;

INSERT INTO role_account(account_id,role_id)
VALUES
    (1,1),
    (2,2)
;
