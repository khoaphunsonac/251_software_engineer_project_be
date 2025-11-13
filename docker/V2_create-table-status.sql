CREATE TABLE `status` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,         -- tên hiển thị: Hoạt động, Ngừng hoạt động...
  `description` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_status_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
INSERT INTO `status` (`name`, `description`) VALUES
('ACTIVE', 'Trạng thái hoạt động'),
('INACTIVE', 'Trạng thái ngừng hoạt động');