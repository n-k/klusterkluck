CREATE TABLE `kf_connectors` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `display_name` varchar(255) DEFAULT NULL,
  `exposed` bit(1) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `port` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `user_namespaces` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `display_name` varchar(255) DEFAULT NULL,
  `git_password` varchar(255) DEFAULT NULL,
  `git_user` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `users` (
  `email` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `iam_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `users_namespaces` (
  `user_email` varchar(255) NOT NULL,
  `namespaces_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_6yof4oulbhpxk9qkt529rlx1i` (`namespaces_id`),
  KEY `FKpfeg9cu9bi0a19v2tbnvilbar` (`user_email`),
  CONSTRAINT `FK5qaca57rgo8fhg174yhjjg9w` FOREIGN KEY (`namespaces_id`) REFERENCES `user_namespaces` (`id`),
  CONSTRAINT `FKpfeg9cu9bi0a19v2tbnvilbar` FOREIGN KEY (`user_email`) REFERENCES `users` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `kf_flows` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contents` longtext,
  `display_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `owner_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4747wuta0nfy5orwwfu17lfki` (`owner_id`),
  CONSTRAINT `FK4747wuta0nfy5orwwfu17lfki` FOREIGN KEY (`owner_id`) REFERENCES `user_namespaces` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `kf_functions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `commit_id` varchar(255) DEFAULT NULL,
  `deployment` varchar(255) DEFAULT NULL,
  `git_url` varchar(255) DEFAULT NULL,
  `ingress_url` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `namespace` varchar(255) DEFAULT NULL,
  `service` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `owner_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhldjo3p4ej3u5soblf31hrk2v` (`owner_id`),
  CONSTRAINT `FKhldjo3p4ej3u5soblf31hrk2v` FOREIGN KEY (`owner_id`) REFERENCES `user_namespaces` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
