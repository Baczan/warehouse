-- phpMyAdmin SQL Dump
-- version 5.0.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 01 Sie 2021, 13:36
-- Wersja serwera: 10.4.14-MariaDB
-- Wersja PHP: 7.2.34

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `auth`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `authority`
--

CREATE TABLE `authority` (
  `id` int(11) NOT NULL,
  `role` varchar(45) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `authority`
--

INSERT INTO `authority` (`id`, `role`, `user_id`) VALUES
(1, 'ROLE_USER', 1),
(2, 'ROLE_USER', 2),
(3, 'ROLE_USER', 3),
(4, 'ROLE_USER', 4),
(5, 'ROLE_USER', 5),
(6, 'ROLE_USER', 6);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `client`
--

CREATE TABLE `client` (
  `id` int(11) NOT NULL,
  `client_id` varchar(256) NOT NULL,
  `client_secret` text NOT NULL,
  `client_secret_plain` text NOT NULL,
  `require_user_consent` tinyint(1) NOT NULL,
  `require_proof_key` tinyint(1) NOT NULL,
  `reuse_refresh_tokens` tinyint(1) NOT NULL,
  `access_token_time_to_live` varchar(256) NOT NULL,
  `refresh_token_time_to_live` varchar(256) NOT NULL,
  `name` varchar(256) NOT NULL,
  `owner` int(11) NOT NULL,
  `authentication_method_basic` tinyint(1) NOT NULL,
  `authentication_method_post` tinyint(1) NOT NULL,
  `authentication_method_none` tinyint(1) NOT NULL,
  `authorization_grant_type_authorization_code` tinyint(1) NOT NULL,
  `authorization_grant_type_implicit` tinyint(1) NOT NULL,
  `authorization_grant_type_refresh_token` tinyint(1) NOT NULL,
  `authorization_grant_type_password` tinyint(1) NOT NULL,
  `authorization_grant_type_client_credentials` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `client`
--

INSERT INTO `client` (`id`, `client_id`, `client_secret`, `client_secret_plain`, `require_user_consent`, `require_proof_key`, `reuse_refresh_tokens`, `access_token_time_to_live`, `refresh_token_time_to_live`, `name`, `owner`, `authentication_method_basic`, `authentication_method_post`, `authentication_method_none`, `authorization_grant_type_authorization_code`, `authorization_grant_type_implicit`, `authorization_grant_type_refresh_token`, `authorization_grant_type_password`, `authorization_grant_type_client_credentials`) VALUES
(2, '6e4b33c4-1419-4fd4-bdab-37a9c55cf990', '$2a$10$7M0zFo7x.mgfOIgxg1tSWuCx8oruSqsikhf9grg56e3uT8VxqMjvG', 'dc28003e-5595-4382-ba43-03a7d3fc0dab', 0, 0, 0, 'PT30M', 'PT168H', 'bachanwiktor', 1, 1, 0, 0, 1, 0, 0, 0, 0),
(4, '2e07887f-7b44-42eb-b870-6b098391aec0', '$2a$10$ejXAv3W.uwE34/qAHiNoguUtLYtOknuJxtnwAnyDVXaYp5Gtxq7eW', '9f3191de-5830-45d6-b7f5-52a3959ac547', 0, 0, 0, 'PT30M', 'PT168H', 'Drive', 5, 1, 0, 0, 1, 0, 0, 0, 0);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `oauth2_authorization`
--

CREATE TABLE `oauth2_authorization` (
  `id` varchar(100) NOT NULL,
  `registered_client_id` varchar(100) NOT NULL,
  `principal_name` varchar(200) NOT NULL,
  `authorization_grant_type` varchar(100) NOT NULL,
  `attributes` varchar(4000) DEFAULT NULL,
  `state` varchar(1000) DEFAULT NULL,
  `authorization_code_value` blob DEFAULT NULL,
  `authorization_code_issued_at` timestamp NULL DEFAULT NULL,
  `authorization_code_expires_at` timestamp NULL DEFAULT NULL,
  `authorization_code_metadata` varchar(1000) DEFAULT NULL,
  `access_token_value blob` blob DEFAULT NULL,
  `access_token_issued_at` timestamp NULL DEFAULT NULL,
  `access_token_expires_at` timestamp NULL DEFAULT NULL,
  `access_token_metadata` varchar(1000) DEFAULT NULL,
  `access_token_type` varchar(100) DEFAULT NULL,
  `access_token_scopes` varchar(1000) DEFAULT NULL,
  `oidc_id_token_value` blob DEFAULT NULL,
  `oidc_id_token_issued_at` timestamp NULL DEFAULT NULL,
  `oidc_id_token_expires_at` timestamp NULL DEFAULT NULL,
  `oidc_id_token_metadata` varchar(1000) DEFAULT NULL,
  `refresh_token_value` blob DEFAULT NULL,
  `refresh_token_issued_at` timestamp NULL DEFAULT NULL,
  `refresh_token_expires_at` timestamp NULL DEFAULT NULL,
  `refresh_token_metadata` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `password_change_token`
--

CREATE TABLE `password_change_token` (
  `id` binary(16) NOT NULL,
  `email` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `redirect_url`
--

CREATE TABLE `redirect_url` (
  `id` int(11) NOT NULL,
  `client_id` int(11) NOT NULL,
  `redirect_url` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `redirect_url`
--

INSERT INTO `redirect_url` (`id`, `client_id`, `redirect_url`) VALUES
(12, 4, 'http://localhost/'),
(14, 2, 'http://localhost/'),
(16, 4, 'http://localhost/');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `refresh_token`
--

CREATE TABLE `refresh_token` (
  `id` binary(16) NOT NULL,
  `user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `register_token`
--

CREATE TABLE `register_token` (
  `id` binary(16) NOT NULL,
  `email` text NOT NULL,
  `password` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `register_token`
--

INSERT INTO `register_token` (`id`, `email`, `password`) VALUES
(0x52922f073e6544a58b4101ab1ef647fd, 'ontorio@onet.eu', '$2a$10$NQsx83J4..Vp4JwvQ4vtTuGWwzKej4KjkEu3XUFu5OytMahBa3Qfm'),
(0x52f30ca8b9fc419183e3c4c4f2421337, 'bachanwiktor1@gmail.com', '$2a$10$SQxedEXPYfmkp9fDaPk4xudFHJMU9ITvZm9Q54bX38oCBX3eLocQq'),
(0x472d2f1e3ba6401ea3b2b103ea0a867b, 'bachanwiktor1@gmail.com', '$2a$10$UNQFZD0aBYfEWnqwRnIeZ.pfy3aC9kF5z0p/hzjUVb9TD2rpohFV6'),
(0x6600a5638b684cc19ed4fc4d9e096c50, 'ontorio@onet.eu', '$2a$10$tMmsCHY.f/qSV4QEMtbpvOywZjA1X1jS.B1qy37V7PG1SIqLhl7wS'),
(0xca01f5fb8a374216840c0edc22759e51, 'ontorio@onet.eu', '$2a$10$Xzk58EEtOLELcXk5l/CpAehE083LIom6J8ZWBMONeWMhtICZpmXlm');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `role`
--

CREATE TABLE `role` (
  `id` int(11) NOT NULL,
  `role_template_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `client_id` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `role`
--

INSERT INTO `role` (`id`, `role_template_id`, `user_id`, `client_id`) VALUES
(1, 5, 2, '6e4b33c4-1419-4fd4-bdab-37a9c55cf990'),
(2, 4, 2, '6e4b33c4-1419-4fd4-bdab-37a9c55cf990'),
(3, 3, 1, '6e4b33c4-1419-4fd4-bdab-37a9c55cf990'),
(4, 4, 1, '6e4b33c4-1419-4fd4-bdab-37a9c55cf990'),
(5, 5, 1, '6e4b33c4-1419-4fd4-bdab-37a9c55cf990'),
(7, 8, 2, '6e4b33c4-1419-4fd4-bdab-37a9c55cf990'),
(8, 11, 5, '2e07887f-7b44-42eb-b870-6b098391aec0'),
(9, 12, 6, '2e07887f-7b44-42eb-b870-6b098391aec0');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `role_template`
--

CREATE TABLE `role_template` (
  `id` int(11) NOT NULL,
  `client_id` varchar(256) NOT NULL,
  `default_role` tinyint(1) NOT NULL,
  `owner_only` tinyint(1) NOT NULL,
  `role` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `role_template`
--

INSERT INTO `role_template` (`id`, `client_id`, `default_role`, `owner_only`, `role`) VALUES
(3, '6e4b33c4-1419-4fd4-bdab-37a9c55cf990', 1, 1, 'MANAGER'),
(4, '6e4b33c4-1419-4fd4-bdab-37a9c55cf990', 1, 0, 'ADMIN'),
(5, '6e4b33c4-1419-4fd4-bdab-37a9c55cf990', 0, 0, 'MODERATOR'),
(8, '6e4b33c4-1419-4fd4-bdab-37a9c55cf990', 0, 0, 'SUPPORT'),
(9, '2e07887f-7b44-42eb-b870-6b098391aec0', 1, 1, 'MANAGER'),
(10, '2e07887f-7b44-42eb-b870-6b098391aec0', 1, 0, 'ADMIN'),
(11, '2e07887f-7b44-42eb-b870-6b098391aec0', 0, 0, 'SUPPORT'),
(12, '2e07887f-7b44-42eb-b870-6b098391aec0', 0, 0, 'FAS');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `spring_session`
--

CREATE TABLE `spring_session` (
  `PRIMARY_ID` char(36) NOT NULL,
  `SESSION_ID` char(36) NOT NULL,
  `CREATION_TIME` bigint(20) NOT NULL,
  `LAST_ACCESS_TIME` bigint(20) NOT NULL,
  `MAX_INACTIVE_INTERVAL` int(11) NOT NULL,
  `EXPIRY_TIME` bigint(20) NOT NULL,
  `PRINCIPAL_NAME` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

--
-- Zrzut danych tabeli `spring_session`
--

INSERT INTO `spring_session` (`PRIMARY_ID`, `SESSION_ID`, `CREATION_TIME`, `LAST_ACCESS_TIME`, `MAX_INACTIVE_INTERVAL`, `EXPIRY_TIME`, `PRINCIPAL_NAME`) VALUES
('f4998fa4-0fab-48da-ab91-2b22bbb6cf4c', '98860e48-f939-4543-869c-a888d8987a19', 1627586235455, 1627586533154, 28800, 1627615333154, '5');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `spring_session_attributes`
--

CREATE TABLE `spring_session_attributes` (
  `SESSION_PRIMARY_ID` char(36) NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

--
-- Zrzut danych tabeli `spring_session_attributes`
--

INSERT INTO `spring_session_attributes` (`SESSION_PRIMARY_ID`, `ATTRIBUTE_NAME`, `ATTRIBUTE_BYTES`) VALUES
('f4998fa4-0fab-48da-ab91-2b22bbb6cf4c', 'org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN', 0xaced0005737200366f72672e737072696e676672616d65776f726b2e73656375726974792e7765622e637372662e44656661756c7443737266546f6b656e5aefb7c82fa2fbd50200034c000a6865616465724e616d657400124c6a6176612f6c616e672f537472696e673b4c000d706172616d657465724e616d6571007e00014c0005746f6b656e71007e0001787074000c582d435352462d544f4b454e7400055f6373726674002430333661336439612d623431362d343639362d623932632d363663633731303861356430),
('f4998fa4-0fab-48da-ab91-2b22bbb6cf4c', 'SPRING_SECURITY_CONTEXT', 0xaced00057372003d6f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e636f6e746578742e5365637572697479436f6e74657874496d706c000000000000021c0200014c000e61757468656e7469636174696f6e7400324c6f72672f737072696e676672616d65776f726b2f73656375726974792f636f72652f41757468656e7469636174696f6e3b78707372004f6f72672e737072696e676672616d65776f726b2e73656375726974792e61757468656e7469636174696f6e2e557365726e616d6550617373776f726441757468656e7469636174696f6e546f6b656e000000000000021c0200024c000b63726564656e7469616c737400124c6a6176612f6c616e672f4f626a6563743b4c00097072696e636970616c71007e0004787200476f72672e737072696e676672616d65776f726b2e73656375726974792e61757468656e7469636174696f6e2e416273747261637441757468656e7469636174696f6e546f6b656ed3aa287e6e47640e0200035a000d61757468656e746963617465644c000b617574686f7269746965737400164c6a6176612f7574696c2f436f6c6c656374696f6e3b4c000764657461696c7371007e0004787001737200266a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c654c697374fc0f2531b5ec8e100200014c00046c6973747400104c6a6176612f7574696c2f4c6973743b7872002c6a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c65436f6c6c656374696f6e19420080cb5ef71e0200014c00016371007e00067870737200136a6176612e7574696c2e41727261794c6973747881d21d99c7619d03000149000473697a6578700000000177040000000173720026636f6d2e6578616d706c652e706b63652e6d6f64656c732e417574686f726974794d6f64656c71c70d43e9f7a4690200014c0004726f6c657400124c6a6176612f6c616e672f537472696e673b7870740009524f4c455f555345527871007e000d737200486f72672e737072696e676672616d65776f726b2e73656375726974792e7765622e61757468656e7469636174696f6e2e57656241757468656e7469636174696f6e44657461696c73000000000000021c0200024c000d72656d6f74654164647265737371007e000f4c000973657373696f6e496471007e000f78707400093132372e302e302e3174002435363737313535392d616436642d343137392d383065622d3437313734306265366433637073720021636f6d2e6578616d706c652e706b63652e6d6f64656c732e456d61696c55736572dc58ac70e394064d0200044c000b617574686f72697469657371007e00064c000870617373776f726471007e000f4c0004757365727400204c636f6d2f6578616d706c652f706b63652f656e7469746965732f557365723b4c0008757365726e616d6571007e000f78707371007e000c0000000177040000000171007e00107874003c2432612431302448617350554a3441632f783833374f692f4d6837577565443231576f4a4536514951306142723533795138704a6a6643414973514b7372001e636f6d2e6578616d706c652e706b63652e656e7469746965732e557365721028b159af7b3b050200095a000e656d61696c41637469766174656449000269644c000b617574686f72697469657371007e00094c00096372656174656441747400104c6a6176612f7574696c2f446174653b4c0005656d61696c71007e000f4c000a66616365626f6f6b496471007e000f4c0008676f6f676c65496471007e000f4c000870617373776f726471007e000f4c000b7573657244657461696c737400274c636f6d2f6578616d706c652f706b63652f656e7469746965732f5573657244657461696c733b787001000000057372002f6f72672e68696265726e6174652e636f6c6c656374696f6e2e696e7465726e616c2e50657273697374656e74426167fe57c5afda4fa6440200024c000362616771007e00094c001270726f7669646564436f6c6c656374696f6e71007e00067872003e6f72672e68696265726e6174652e636f6c6c656374696f6e2e696e7465726e616c2e416273747261637450657273697374656e74436f6c6c656374696f6e5718b75d8aba735402000b5a001b616c6c6f774c6f61644f7574736964655472616e73616374696f6e49000a63616368656453697a655a000564697274795a000e656c656d656e7452656d6f7665645a000b696e697469616c697a65645a000d697354656d7053657373696f6e4c00036b65797400164c6a6176612f696f2f53657269616c697a61626c653b4c00056f776e657271007e00044c0004726f6c6571007e000f4c001273657373696f6e466163746f72795575696471007e000f4c000e73746f726564536e617073686f7471007e0021787000ffffffff00000100737200116a6176612e6c616e672e496e746567657212e2a0a4f781873802000149000576616c7565787200106a6176612e6c616e672e4e756d62657286ac951d0b94e08b02000078700000000571007e001e74002a636f6d2e6578616d706c652e706b63652e656e7469746965732e557365722e617574686f726974696573707371007e000c0000000177040000000173720023636f6d2e6578616d706c652e706b63652e656e7469746965732e417574686f726974794fb89b3bf4f4e22902000349000269644c0004726f6c6571007e000f4c0007757365725f696471007e001778700000000571007e001171007e001e787371007e000c0000000177040000000171007e00297870737200126a6176612e73716c2e54696d657374616d702618d5c80153bf650200014900056e616e6f737872000e6a6176612e7574696c2e44617465686a81014b597419030000787077080000017adae1dc68780000000074001662616368616e77696b746f7240676d61696c2e636f6d7074001531303432353234313739323035383235313730313571007e001a73720025636f6d2e6578616d706c652e706b63652e656e7469746965732e5573657244657461696c73f34af8569288c87602000449000269644c000966697273744e616d6571007e000f4c00086c6173744e616d6571007e000f4c000570686f6e6571007e000f78700000000574000757696b746f727274000642616368616e74000937323735363432313474000135);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `email` text NOT NULL,
  `email_activated` tinyint(1) NOT NULL,
  `password` text DEFAULT NULL,
  `google_id` varchar(256) DEFAULT NULL,
  `facebook_id` varchar(256) DEFAULT NULL,
  `user_details_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `user`
--

INSERT INTO `user` (`id`, `email`, `email_activated`, `password`, `google_id`, `facebook_id`, `user_details_id`, `created_at`) VALUES
(5, 'bachanwiktor@gmail.com', 1, '$2a$10$HasPUJ4Ac/x837Oi/Mh7WueD21WoJE6QIQ0aBr53yQ8pJjfCAIsQK', '104252417920582517015', NULL, 5, '2021-07-24 21:37:53'),
(6, 'ontorio@onet.eu', 0, NULL, NULL, '4391656190880136', 6, '2021-07-24 22:05:21');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `user_details`
--

CREATE TABLE `user_details` (
  `id` int(11) NOT NULL,
  `first_name` varchar(256) DEFAULT '',
  `last_name` varchar(256) DEFAULT '',
  `phone` varchar(100) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `user_details`
--

INSERT INTO `user_details` (`id`, `first_name`, `last_name`, `phone`) VALUES
(1, 'Wiktor', 'Bachan', '727564214'),
(2, '', '', ''),
(3, '', '', ''),
(4, '', '', ''),
(5, 'Wiktorr', 'Bachan', '727564214'),
(6, '', '', '');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `authority`
--
ALTER TABLE `authority`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `client`
--
ALTER TABLE `client`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `oauth2_authorization`
--
ALTER TABLE `oauth2_authorization`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `redirect_url`
--
ALTER TABLE `redirect_url`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `role_template`
--
ALTER TABLE `role_template`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `spring_session`
--
ALTER TABLE `spring_session`
  ADD PRIMARY KEY (`PRIMARY_ID`),
  ADD UNIQUE KEY `SPRING_SESSION_IX1` (`SESSION_ID`),
  ADD KEY `SPRING_SESSION_IX2` (`EXPIRY_TIME`),
  ADD KEY `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`);

--
-- Indeksy dla tabeli `spring_session_attributes`
--
ALTER TABLE `spring_session_attributes`
  ADD PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`);

--
-- Indeksy dla tabeli `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `user_details`
--
ALTER TABLE `user_details`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT dla zrzuconych tabel
--

--
-- AUTO_INCREMENT dla tabeli `authority`
--
ALTER TABLE `authority`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT dla tabeli `client`
--
ALTER TABLE `client`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT dla tabeli `redirect_url`
--
ALTER TABLE `redirect_url`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT dla tabeli `role`
--
ALTER TABLE `role`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT dla tabeli `role_template`
--
ALTER TABLE `role_template`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT dla tabeli `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT dla tabeli `user_details`
--
ALTER TABLE `user_details`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `spring_session_attributes`
--
ALTER TABLE `spring_session_attributes`
  ADD CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `spring_session` (`PRIMARY_ID`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
