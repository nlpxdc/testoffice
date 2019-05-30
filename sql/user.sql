SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
  `user_id`     bigint(20)   NOT NULL auto_increment,
  `name`        varchar(255) NOT NULL,
  `gender`      tinyint,
  `age`         smallint,
  `role_id`     int,
  `salary`      double(10, 2),
  `bonus`       float(5, 2),
  `enabled`     bit,
  `create_time` datetime,
  PRIMARY KEY (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  auto_increment = 1;
