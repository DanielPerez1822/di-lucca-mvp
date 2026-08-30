-- ====================================================================
-- SCRIPT DE INSERCIÓN DE DATOS DE PRUEBA - CONSULTORIO DENTAL DI-LUCCA
-- ====================================================================

-- 1. Insertar Catálogo de Roles
INSERT INTO roles (id, name, description)
VALUES 
    ('a0000000-0000-0000-0000-000000000001', 'ADMIN', 'Administrador General del Consultorio DI-LUCCA'),
    ('a0000000-0000-0000-0000-000000000002', 'DENTIST', 'Odontólogo / Especialista Dental'),
    ('a0000000-0000-0000-0000-000000000003', 'SECRETARY_ASSISTANT', 'Recepcionista / Auxiliar del Consultorio')
ON CONFLICT (name) DO NOTHING;

-- 2. Insertar Usuarios por Defecto (Contraseña para todos: Admin@12345 / Dentist@12345 / Receptionist@12345)
-- Hashes BCrypt pre-generados
INSERT INTO users (id, email, password_hash, first_name, last_name, phone, document_number, is_active, created_at, updated_at)
VALUES 
    ('11111111-1111-1111-1111-111111111111', 'admin@dilucca.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd0P1R7Xc9GjI366', 'Administrador', 'DI-LUCCA', '+57 3000000000', '1000000001', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('22222222-2222-2222-2222-222222222222', 'dentista@dilucca.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd0P1R7Xc9GjI366', 'Dr. Carlos', 'Mendoza', '+57 3100000000', '1000000002', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('33333333-3333-3333-3333-333333333333', 'recepcion@dilucca.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd0P1R7Xc9GjI366', 'Laura', 'Gómez (Recepcionista)', '+57 3200000000', '1000000003', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (email) DO NOTHING;

-- 3. Asignar Roles a Usuarios
INSERT INTO user_roles (user_id, role_id)
VALUES 
    ('11111111-1111-1111-1111-111111111111', 'a0000000-0000-0000-0000-000000000001'), -- Admin -> ADMIN
    ('22222222-2222-2222-2222-222222222222', 'a0000000-0000-0000-0000-000000000002'), -- Dr. Carlos -> DENTIST
    ('33333333-3333-3333-3333-333333333333', 'a0000000-0000-0000-0000-000000000003')  -- Laura -> EMPLOYEE
ON CONFLICT DO NOTHING;

-- 4. Insertar Pacientes de Ejemplo
INSERT INTO patients (id, first_name, last_name, document_type, document_number, phone, email, date_of_birth, address, is_active, created_at, updated_at)
VALUES 
    ('b1111111-1111-1111-1111-111111111111', 'Juan', 'Pérez', 'CC', '1012345678', '+57 3111234567', 'juan.perez@example.com', '1990-05-15', 'Calle 45 # 12-34', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('b2222222-2222-2222-2222-222222222222', 'María', 'Rodríguez', 'CC', '1023456789', '+57 3122345678', 'maria.rodriguez@example.com', '1985-08-22', 'Carrera 15 # 88-10', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('b3333333-3333-3333-3333-333333333333', 'Andrés', 'López', 'TI', '1034567890', '+57 3133456789', 'andres.lopez@example.com', '2008-11-03', 'Avenida El Dorado # 68-50', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (document_number) DO NOTHING;

-- 5. Insertar Catálogo Inicial de Procedimientos
INSERT INTO procedures (id, name, description, price, duration, is_active, created_at, updated_at)
VALUES 
    ('c1111111-1111-1111-1111-111111111111', 'Consulta general', 'Consulta odontológica de valoración general', 50000.00, 30, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('c2222222-2222-2222-2222-222222222222', 'Limpieza dental profiláctica', 'Limpieza dental con ultrasonido y pulido', 80000.00, 30, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('c3333333-3333-3333-3333-333333333333', 'Aplicación de flúor', 'Aplicación tópica de flúor para prevención de caries', 30000.00, 15, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('c4444444-4444-4444-4444-444444444444', 'Obturación con resina', 'Calza dental con resina compuesta fotopolimerizable', 120000.00, 45, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('c5555555-5555-5555-5555-555555555555', 'Endodoncia (conducto)', 'Tratamiento de conducto radicular', 350000.00, 60, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('c6666666-6666-6666-6666-666666666666', 'Blanqueamiento dental', 'Blanqueamiento dental con luz LED y peróxido', 350000.00, 60, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;
