INSERT INTO users.users (dni, name, last_name, email, password, phone_number, role, university)
VALUES ('0000000001', 'Admin', 'Nacional', 'admin@unacional.edu.co', '$2b$10$lg7DsUUlaW6EbAWVpqYGlet2NV0KvMdZo93wwaWUQ57VF6u9691VG', '+573012171281', 'ADMIN', 'UNIVERSIDAD_NACIONAL')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users.users (dni, name, last_name, email, password, phone_number, role, university)
VALUES ('0000000002', 'Admin', 'Antioquia', 'admin@udea.edu.co', '$2b$10$lg7DsUUlaW6EbAWVpqYGlet2NV0KvMdZo93wwaWUQ57VF6u9691VG', '+573012171281', 'ADMIN', 'UNIVERSIDAD_DE_ANTIOQUIA')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users.users (dni, name, last_name, email, password, phone_number, role, university)
VALUES ('0000000003', 'Admin', 'Eafit', 'admin@eafit.edu.co', '$2b$10$lg7DsUUlaW6EbAWVpqYGlet2NV0KvMdZo93wwaWUQ57VF6u9691VG', '+573012171281', 'ADMIN', 'UNIVERSIDAD_EAFIT')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users.users (dni, name, last_name, email, password, phone_number, role, university)
VALUES ('0000000004', 'Admin', 'Andes', 'admin@uniandes.edu.co', '$2b$10$lg7DsUUlaW6EbAWVpqYGlet2NV0KvMdZo93wwaWUQ57VF6u9691VG', '+573012171281', 'ADMIN', 'UNIVERSIDAD_DE_LOS_ANDES')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users.users (dni, name, last_name, email, password, phone_number, role, university)
VALUES ('0000000005', 'Admin', 'Bolivariana', 'admin@upb.edu.co', '$2b$10$lg7DsUUlaW6EbAWVpqYGlet2NV0KvMdZo93wwaWUQ57VF6u9691VG', '+573012171281', 'ADMIN', 'UNIVERSIDAD_PONTIFICIA_BOLIVARIANA')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users.users (dni, name, last_name, email, password, phone_number, role, university)
VALUES ('0000000006', 'Admin', 'ITM', 'admin@itm.edu.co', '$2b$10$lg7DsUUlaW6EbAWVpqYGlet2NV0KvMdZo93wwaWUQ57VF6u9691VG', '+573012171281', 'ADMIN', 'ITM')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users.users (dni, name, last_name, email, password, phone_number, role, university)
VALUES ('0000000007', 'Admin', 'Pascual', 'admin@pascualbravo.edu.co', '$2b$10$lg7DsUUlaW6EbAWVpqYGlet2NV0KvMdZo93wwaWUQ57VF6u9691VG', '+573012171281', 'ADMIN', 'PASCUAL_BRAVO')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users.users (dni, name, last_name, email, password, phone_number, role, university)
VALUES ('0000000008', 'Admin', 'Colmayor', 'admin@colmayor.edu.co', '$2b$10$lg7DsUUlaW6EbAWVpqYGlet2NV0KvMdZo93wwaWUQ57VF6u9691VG', '+573012171281', 'ADMIN', 'COLMAYOR')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users.users (dni, name, last_name, email, password, phone_number, role, university)
VALUES ('0000000009', 'Admin', 'Remington', 'admin@uniremington.edu.co', '$2b$10$lg7DsUUlaW6EbAWVpqYGlet2NV0KvMdZo93wwaWUQ57VF6u9691VG', '+573012171281', 'ADMIN', 'UNIREMINGTON')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users.users (dni, name, last_name, email, password, phone_number, role, university)
VALUES ('0000000010', 'Admin', 'Medellin', 'admin@udem.edu.co', '$2b$10$lg7DsUUlaW6EbAWVpqYGlet2NV0KvMdZo93wwaWUQ57VF6u9691VG', '+573012171281', 'ADMIN', 'UNIVERSIDAD_DE_MEDELLIN')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users.users (dni, name, last_name, email, password, phone_number, role, university)
VALUES ('0000000011', 'Admin', 'CES', 'admin@ces.edu.co', '$2b$10$lg7DsUUlaW6EbAWVpqYGlet2NV0KvMdZo93wwaWUQ57VF6u9691VG', '+573012171281', 'ADMIN', 'UNIVERSIDAD_CES')
    ON CONFLICT (email) DO NOTHING;