INSERT INTO datacore (hcmut_id, major_id) VALUES
    ('NV001',     1),
    ('GV001',     2),
    ('17123456',  4),
    ('20123456',  6),
    ('20125678',  7);

UPDATE `user` u
JOIN datacore d ON d.hcmut_id = u.hcmut_id
SET u.major_id = d.major_id;
