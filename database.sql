--database qlbd

create table Users(
    id int AUTO_INCREMENT primary key,
    username varchar(20) unique,
    password varchar(100) not null,
    role varchar(15),
    ENABLE TINYINT(4)
)

create table GiaiDau(
    maGD varchar(10) primary key,
    ten varchar(50)
)

create table BangDau(
    ma_bang int AUTO_INCREMENT primary key,
    ten_bang varchar(10),
    maGD varchar(10),
    FOREIGN KEY (maGD) REFERENCES giaidau(maGD)
)

create table DoiBong(
    id int AUTO_INCREMENT primary key,
    name varchar(50)
)

create table BangDau_DoiBong(
    ma_bang int,
    id int,
    primary KEY(ma_bang,id),
    diem int,
    FOREIGN KEY (ma_bang) REFERENCES BangDau(ma_bang),
    FOREIGN KEY (id) REFERENCES DoiBong(id)
)

create table TranDau(
    maTD int AUTO_INCREMENT primary key,
    ma_bang int,
    FOREIGN KEY (ma_bang) REFERENCES BangDau(ma_bang)
)

create table TranDau_DoiBong(
    maTD int,
    maDB int,
    banthang int,
    FOREIGN KEY (maTD) REFERENCES TranDau(maTD),
    FOREIGN KEY (maDB) REFERENCES DoiBong(id),
    PRIMARY KEY(maTD, maDB)
)

--password: 123
INSERT INTO users VALUES (1,'admin','$2a$10$rBetqQ796xXhXm.JQQRB/.0c.63I/c7LWb4y9Qe5O5Wve72fyP.pe','ROLE_ADMIN',1)