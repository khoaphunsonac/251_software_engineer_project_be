SET FOREIGN_KEY_CHECKS = 0;

-- 1) Tạo bảng phụ N–N
CREATE TABLE `tutor_profile_subject` (
  `tutor_profile_id` BIGINT UNSIGNED NOT NULL,
  `subject_id`       BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`tutor_profile_id`, `subject_id`),
  KEY `idx_tps_subject` (`subject_id`),
  CONSTRAINT `fk_tps_tutor`
    FOREIGN KEY (`tutor_profile_id`) REFERENCES `tutor_profile`(`id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_tps_subject`
    FOREIGN KEY (`subject_id`) REFERENCES `subject`(`id`)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2) (Tuỳ chọn) migrate dữ liệu từ cột cũ subject_id sang bảng phụ
INSERT INTO `tutor_profile_subject` (`tutor_profile_id`, `subject_id`)
SELECT `id`, `subject_id` FROM `tutor_profile` WHERE `subject_id` IS NOT NULL;

-- 3) Bỏ quan hệ cũ 1–N: drop cột subject_id ở tutor_profile
ALTER TABLE `tutor_profile`
  DROP FOREIGN KEY `fk_tutor_profile_subject`,
  DROP COLUMN `subject_id`;


SET FOREIGN_KEY_CHECKS = 1;