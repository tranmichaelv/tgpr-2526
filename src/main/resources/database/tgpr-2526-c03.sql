drop database if exists `tgpr-2526-c03`;
create database if not exists `tgpr-2526-c03`;
use `tgpr-2526-c03`;

create table users
(
    id         int primary key auto_increment,
    last_name  varchar(255)                        not null,
    first_name varchar(255)                        not null,
    email      varchar(255) unique                 not null,
    password   varchar(255)                        not null,
    phone      varchar(20),
    role       enum ('client', 'employee', 'boss') not null,
    blocked    boolean                             not null default false
);

insert into users (id, last_name, first_name, email, password, phone, role, blocked)
values (1, 'Diop', 'Awa', 'awa.diop@email.com', 'Password1,', '0476/01.02.03', 'client', false),
       (2, 'Benkirane', 'Yassine', 'yassine.benkirane@email.com', 'Password1,', '0482/04.05.06', 'client', false),
       (3, 'Van den Broeck', 'Jeroen', 'jeroen.vandenbroeck@email.com', 'Password1,', '0477/07.08.09', 'client', false),
       (4, 'Dubois', 'Camille', 'camille.dubois@email.com', 'Password1,', '0492/10.11.12', 'client', false),
       (5, 'Traoré', 'Moussa', 'moussa.traore@email.com', 'Password1,', '0492/13.14.15', 'client', false),
       (6, 'El Amrani', 'Leïla', 'leila.elamrani@email.com', 'Password1,', '0475/16.17.18', 'employee', false),
       (7, 'Peeters', 'Sofie', 'sofie.peeters@email.com', 'Password1,', '0482/19.20.21', 'employee', false),
       (8, 'Lefebvre', 'Luc', 'luc.lefebvre@email.com', 'Password1,', '0492/22.23.24', 'boss', false);

create table tables
(
    id       int primary key auto_increment,
    capacity int             not null,
    location varchar(255)    not null
);

insert into tables (capacity, location)
values (4, 'Salle principale'),
       (2, 'Salle principale'),
       (6, 'Terrasse'),
       (4, 'Terrasse'),
       (8, 'Salle VIP'),
       (2, 'Salle principale'),
       (4, 'Salle principale'),
       (6, 'Terrasse'),
       (2, 'Salle principale'),
       (4, 'Salle principale');

create table products
(
    id          int primary key auto_increment,
    name        varchar(255)                                         not null,
    description text,
    price       decimal(10, 2)                                       not null,
    type        enum ('starter', 'main', 'dessert', 'drink', 'menu') not null,
    archived    boolean                                              not null default false
);

insert into products (id, name, description, price, type, archived)
values (1, 'Salade César', 'Salade avec poulet, croûtons et parmesan', 8.50, 'starter', false),
       (2, 'Soupe du jour', 'Soupe maison préparée chaque jour', 6.00, 'starter', false),
       (3, 'Bruschetta', 'Pain grillé avec tomates et basilic', 7.00, 'starter', false),
       (4, 'Steak frites', 'Steak grillé servi avec frites', 18.00, 'main', false),
       (5, 'Poulet rôti', 'Poulet rôti aux herbes et légumes', 16.50, 'main', false),
       (6, 'Lasagnes', 'Lasagnes maison à la bolognaise', 15.00, 'main', false),
       (7, 'Tiramisu', 'Dessert italien au café et mascarpone', 6.50, 'dessert', false),
       (8, 'Mousse au chocolat', 'Mousse légère au chocolat noir', 5.50, 'dessert', false),
       (9, 'Crème brûlée', 'Crème vanille caramélisée', 6.00, 'dessert', false),
       (10, 'Coca-Cola', 'Boisson gazeuse rafraîchissante', 2.50, 'drink', false),
       (11, 'Eau minérale', 'Eau plate ou pétillante', 2.00, 'drink', false),
       (12, 'Vin rouge', 'Verre de vin rouge maison', 4.00, 'drink', false),
       (13, 'Menu classique', 'Entrée, plat et dessert au choix', 25.00, 'menu', false),
       (14, 'Menu végétarien', 'Entrée, plat et dessert végétariens', 23.00, 'menu', false),
       (15, 'Menu enfant', 'Plat et dessert adaptés aux enfants', 12.00, 'menu', false),
       (16, 'Lasagnes végétariennes', 'Lasagnes aux légumes et tofu', 15.00, 'main', false),
       (17, 'Salade de fruits', 'Dessert frais de fruits de saison', 5.50, 'dessert', false),
       (18, 'Spaghetti bolognaise', 'Spaghetti avec sauce bolognaise maison', 10.00, 'main', false),
       (19, 'Vin blanc', 'Verre de vin blanc maison', 4.00, 'drink', false),
       (20, 'Bière', 'Bière blonde pression', 3.00, 'drink', false);

create table menu_items
(
    menu          int not null,
    product       int not null,
    order_in_menu int not null,
    primary key (menu, product),
	unique (menu, order_in_menu),
    foreign key (menu) references products (id) on delete cascade,
    foreign key (product) references products (id) on delete cascade
);

insert into menu_items (menu, product, order_in_menu)
values
    -- Menu classique (id 13): Entrée, plat, dessert
    (13, 1, 1),  -- Salade César
    (13, 4, 2),  -- Steak frites
    (13, 7, 3),  -- Tiramisu

    -- Menu végétarien (id 14): Entrée, plat, dessert
    (14, 3, 1),  -- Bruschetta
    (14, 16, 2), -- Lasagnes végétariennes
    (14, 17, 3), -- Salade de fruits

    -- Menu enfant (id 15): Plat, dessert
    (15, 18, 1), -- Spaghetti bolo
    (15, 8, 2); -- Mousse au chocolat



create table services
(
    id         int primary key auto_increment,
    name       varchar(255)                                                                   not null,
    start_time time                                                                           not null,
    end_time   time                                                                           not null,
    week_day   enum ('lundi', 'mardi', 'mercredi', 'jeudi', 'vendredi', 'samedi', 'dimanche') not null,
    unique (name, week_day)
);

insert into services (id, name, start_time, end_time, week_day)
values (1, 'midi', '12:00', '14:30', 'mardi'),
       (2, 'soir', '19:00', '22:30', 'mardi'),
       (3, 'midi', '12:00', '14:30', 'mercredi'),
       (4, 'soir', '19:00', '22:30', 'mercredi'),
       (5, 'midi', '12:00', '14:30', 'jeudi'),
       (6, 'soir', '19:00', '22:30', 'jeudi'),
       (7, 'midi', '12:00', '14:30', 'vendredi'),
       (8, 'soir', '19:00', '23:00', 'vendredi'),
       (9, 'midi', '12:00', '15:00', 'samedi'),
       (10, 'soir', '19:00', '23:00', 'samedi'),
       (11, 'midi', '12:00', '15:00', 'dimanche');

create table closing_periods
(
    id         int primary key auto_increment,
    start_date date         not null,
    end_date   date         not null,
    reason     varchar(255) not null
);

insert into closing_periods (id, start_date, end_date, reason)
values (1, '2025-08-06', '2025-08-11', 'Congés annuels d\'été'),
       (2, '2025-12-24', '2025-12-26', 'Fermeture pour Noël'),
       (3, '2026-01-01', '2026-01-02', 'Nouvel an'),
       (4, '2026-04-01', '2026-04-03', 'Travaux de maintenance');

create table reservations
(
    id               int primary key auto_increment,
    number_of_people int      not null,
    client           int      not null,
    `table`          int      not null,
    service          int      not null,
    employee         int,
    online           boolean  not null,
    reservation_date date     not null,
    foreign key (client) references users (id) on delete cascade,
    foreign key (`table`) references tables (id) on delete cascade,
    foreign key (employee) references users (id) on delete set null,
    foreign key (service) references services (id) on delete cascade,
    unique (`table`, reservation_date, service)
);

insert into reservations (id, number_of_people, client, `table`, service, employee, online, reservation_date)
values (1, 3, 1, 1, 1, 6, true, '2025-08-05'),   
       (2, 5, 2, 3, 2, 7, false, '2025-08-12'),  
       (3, 3, 3, 1, 3, 6, true, '2025-08-13'),   
       (4, 7, 4, 5, 6, 7, false, '2025-08-14'),  
       (5, 3, 5, 4, 7, 6, true, '2025-08-15'),   
       (6, 6, 1, 3, 10, 7, false, '2025-08-16'), 
       (7, 1, 2, 6, 11, 6, true, '2025-08-17'),  
       (8, 3, 3, 7, 2, 7, false, '2025-09-02'),  
       (9, 3, 1, 1, 3, 6, true, '2025-09-03'),   
       (10, 4, 2, 4, 7, 7, false, '2025-09-05'), 
       (11, 3, 3, 1, 1, 6, true, '2025-09-09'),  
       (12, 7, 4, 5, 2, 7, false, '2025-09-16'), 
       (13, 5, 5, 3, 3, 6, true, '2025-09-17'),  
       (14, 4, 4, 4, 6, 7, false, '2025-10-02'), 
       (15, 7, 3, 5, 7, 6, true, '2025-10-03'),  
       (16, 2, 2, 6, 10, 7, false, '2025-10-11'), 
       (17, 6, 1, 3, 11, 6, true, '2025-10-12'),  
       (18, 5, 2, 8, 2, 7, false, '2025-11-04'), 
       (19, 7, 3, 5, 3, 6, true, '2025-11-05'),  
       (20, 3, 4, 10, 7, 7, false, '2025-11-07'); 

create table orders
(
    id             int primary key auto_increment,
    order_date     date not null,
    client         int      not null,
    `table`        int,
    take_away      bool     not null,
    online         boolean  not null,
    service        int      not null,
    employee       int,
    reservation    int,
    effective_people_count int not null,
    discount       decimal(5, 2),
    payment_method enum ('cash', 'card', 'other'),
    foreign key (client) references users (id) on delete cascade,
    foreign key (`table`) references tables (id) on delete set null,
    foreign key (service) references services (id) on delete cascade,
    foreign key (employee) references users (id) on delete set null,
    foreign key (reservation) references reservations (id) on delete set null
);

insert into orders (id, order_date, client, `table`, take_away, online, service, employee, reservation, effective_people_count, discount,                            
                    payment_method)
values (1, '2025-08-05', 1, 1, false, false, 1, 6, 1, 3, 5.00, 'card'),  
       (2, '2025-08-12', 2, 3, false, false, 2, 7, 2, 5, 0.00, 'cash'),   
       (3, '2025-08-13', 3, 1, false, false, 3, 6, 3, 3, 10.00, 'other'),
       (4, '2025-08-14', 4, 5, false, false, 6, 7, 4, 7, 0.00, 'card'),   
       (5, '2025-08-15', 5, 4, false, false, 7, 6, 5, 3, 2.50, 'cash'), 
       (6, '2025-08-16', 1, 3, false, false, 10, 7, 6, 6, 0.00, 'card'),  
       (7, '2025-09-02', 3, 7, false, false, 2, 7, 8, 3, 0.00, 'cash'),   
       (8, '2025-09-03', 1, 1, false, false, 3, 6, 9, 3, 1.50, 'card'),  
       (9, '2025-09-05', 2, 4, false, false, 7, 7, 10, 4, 0.00, 'other'), 
       (10, '2025-09-09', 3, 1, false, false, 1, 6, 11, 3, 2.00, 'cash'), 
       (11, '2025-09-16', 4, 5, false, false, 2, 7, 12, 7, 0.00, 'card'), 
       (12, '2025-08-17', 3, null, true, true, 11, 6, null, 1, 4.00, 'other'),                                                                             
       (13, '2025-08-19', 4, 7, false, false, 2, 7, null, 2, 0.00, 'cash'),
       (14, '2025-08-19', 5, null, true, true, 2, 6, null, 1, 1.00, 'card'),                                                                               
       (15, '2025-08-20', 1, 8, false, false, 4, 7, null, 2, 0.00, 'other'),                                                                               
       (16, '2025-08-21', 2, null, true, true, 5, 6, null, 1, 2.50, 'cash'),                                                                               
       (17, '2025-08-22', 3, 9, false, false, 7, 7, null, 2, 0.00, 'card'),
       (18, '2025-08-23', 4, null, true, true, 9, 6, null, 1, 3.50, 'other'),                                                                              
       (19, '2025-08-24', 5, 10, false, false, 11, 7, null, 3, 0.00, 'cash'),                                                                              
       (20, '2025-08-26', 1, 1, false, false, 1, 6, null, 2, 0.00, 'card'),
       (21, '2025-08-26', 2, 2, false, false, 2, 7, null, 1, 0.00, 'cash'),
       (22, '2025-08-27', 3, 3, false, false, 3, 6, null, 2, 0.00, 'other'),                                                                               
       (23, '2025-08-28', 4, null, true, false, 5, 6, null, 1, 0.00, 'card'),                                                                              
       (24, '2025-08-29', 5, null, true, false, 8, 7, null, 1, 0.00, 'cash'),
       (25, '2025-08-30', 1, null, true, false, 9, 6, null, 1, 0.00, 'card');

create table order_items
(
    `order`  int            not null,
    product  int            not null,
    quantity int            not null,
    price    decimal(10, 2) not null,
    primary key (`order`, product),
    foreign key (`order`) references orders (id) on delete cascade,
    foreign key (product) references products (id) on delete cascade
);

insert into order_items (`order`, product, quantity, price)
values (1, 1, 2, 8.50),
       (1, 10, 2, 2.50),
       (2, 4, 1, 18.00),
       (2, 12, 1, 4.00),
       (3, 5, 2, 16.50),
       (3, 11, 2, 2.00),
       (4, 6, 1, 15.00),
       (4, 7, 1, 6.50),
       (5, 13, 1, 25.00),
       (6, 2, 2, 6.00),
       (6, 10, 1, 2.50),
       (7, 3, 1, 7.00),
       (7, 8, 1, 5.50),
       (8, 14, 1, 23.00),
       (9, 18, 2, 10.00),
       (10, 9, 1, 6.00),
       (11, 15, 1, 12.00),
       (12, 16, 1, 15.00),
       (13, 17, 1, 5.50),
       (14, 19, 2, 4.00),
       (15, 20, 3, 3.00),
       (16, 1, 1, 8.50),
       (17, 4, 1, 18.00),
       (18, 5, 1, 16.50),
       (19, 6, 1, 15.00),
       (20, 7, 1, 6.50),
       (21, 2, 1, 6.00),
       (21, 11, 2, 2.00),
       (22, 3, 1, 7.00),
       (22, 9, 1, 6.00),
       (23, 4, 1, 18.00),
       (23, 12, 1, 4.00),
       (24, 5, 1, 16.50),
       (24, 8, 1, 5.50),
       (25, 6, 1, 15.00),
       (25, 10, 1, 2.50);

