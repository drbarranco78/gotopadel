-- USE GOTOPADEL;

INSERT IGNORE INTO USUARIO (id_usuario, nombre, email, fecha_nac, genero, nivel, fecha_inscripcion,rol)
VALUES (1, 'admin', 'admin@email.com', NULL, 'Hombre', NULL, NULL, 'ADMIN');

INSERT IGNORE INTO CREDENCIALES (id_usuario, password) 
VALUES (1, 'Gotopadel1');

INSERT IGNORE INTO USUARIO (nombre, email, fecha_nac, genero, nivel, fecha_inscripcion) VALUES 
('Juan Pérez', 'juan@email.com', '15/05/1990', 'Hombre', 'Avanzado', '15/11/2024'),
('María González', 'maria@email.com', '22/08/1985', 'Mujer', 'Intermedio', '25/01/2025'),
('Pedro Ramírez', 'pedro@email.com', '10/02/1995', 'Hombre', 'Principiante', '10/10/2024'),
('Ana Sánchez', 'ana@email.com', '30/11/1988', 'Mujer', 'Avanzado','20/12/2024'),
('Carlos Martínez', 'carlos@email.com', '05/07/1992', 'Hombre', 'Intermedio', '01/12/2024');

INSERT IGNORE INTO CREDENCIALES (id_usuario, password) VALUES 
(2, 'Clavejuan1'),
(3, 'Clavemaria1'),
(4, 'Clavepedro1'),
(5, 'Claveana1'),
(6, 'Clavecarlos1');

INSERT IGNORE INTO UBICACION (nombre, ciudad) VALUES 
('Escuela de Pádel Almería', 'Almería'),
('Complejo Deportivo Granada', 'Granada'),
('Centro Pádel Roquetas', 'Almería'),
('Pádel Mediterraneo', 'Almería');

INSERT IGNORE INTO PARTIDO (id_usuario, tipo_partido, vacantes, nivel, fecha_partido, fecha_publicacion, hora_partido, ubicacion, comentarios) VALUES 
(2, 'Singles', 1, 'Avanzado', '20/12/2024', '10/12/2024', '18:00', 1, 'Partido de práctica'),
(3, 'Dobles', 3, 'Intermedio', '22/11/2024', '15/11/2024', '19:30', 2, 'Buscamos pareja para jugar'),
(4, 'Singles', 1, 'Principiante', '25/01/2025', '10/01/2025', '17:45', 3, 'Partido casual'),
(5, 'Dobles', 3, 'Avanzado', '18/12/2024', '27/11/2024', '20:15', 1, 'Competición amistosa'),
(6, 'Singles', 1, 'Experto', '25/01/2025', '23/12/2024', '16:30', 2, 'Entrenamiento intensivo');

INSERT IGNORE INTO INSCRIBE (id_usuario, id_partido, fecha_ins) VALUES 
(2, 1, '10/12/2024'),
(3, 2, '15/11/2024'),
(4, 3, '15/01/2025'),
(5, 4, '10/12/2024'),
(6, 5, '25/12/2024');
