USE booking_management;

INSERT INTO ACCOUNT(username,password)
VALUES
    ('amin','$2a$12$ayih9HWcXCFc/0uhR4fIX.coCWPdnrYE07HJfOnawKfRAHcpYDOQi'),
    ('user','$2a$12$h1jrIrcuByJFrt4OnLWS5./5R6sfRquG1FqZh2MEASTGtKrsLrNqC')
;

INSERT INTO `ROLE`(role_name)
VALUES
    ('ADMIN'),
    ('USER')
;

INSERT INTO ROLE_ACCOUNT(account_id,role_id)
VALUES
    (1,1),
    (2,2)
;