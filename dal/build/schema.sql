
    create table Certificate_service.GiftCertificate (
       Id bigint not null auto_increment,
        CreateDate datetime,
        Description varchar(255),
        Duration integer,
        LastUpdateDate datetime,
        Name varchar(255),
        Price decimal(19,2),
        primary key (Id)
    ) type=MyISAM

    create table Certificate_service.Purchase (
       Id bigint not null auto_increment,
        Created_on datetime,
        Updated_on datetime,
        Cost decimal(19,2),
        Creation_date datetime,
        Id_user bigint,
        primary key (Id)
    ) type=MyISAM

    create table Certificate_service.Purchase_Certificate (
       Id_order bigint not null,
        Id_certificate bigint not null
    ) type=MyISAM

    create table Certificate_service.Tag (
       Id bigint not null auto_increment,
        Created_on datetime,
        Updated_on datetime,
        Name varchar(255),
        primary key (Id)
    ) type=MyISAM

    create table Certificate_service.Tag_Certificate (
       IdCertificate bigint not null,
        IdTag bigint not null
    ) type=MyISAM

    create table Certificate_service.User (
       Id bigint not null auto_increment,
        Created_on datetime,
        Updated_on datetime,
        Date_of_birth date,
        Email varchar(255),
        First_name varchar(255),
        Last_name varchar(255),
        Login varchar(255),
        Password varchar(255),
        Role varchar(255),
        primary key (Id)
    ) type=MyISAM

    alter table Certificate_service.Purchase 
       add constraint FKs56weium242oxc5rhfu5en2v4 
       foreign key (Id_user) 
       references Certificate_service.User (Id)

    alter table Certificate_service.Purchase_Certificate 
       add constraint FKtklff637petfiaqx2b36f630m 
       foreign key (Id_certificate) 
       references Certificate_service.GiftCertificate (Id)

    alter table Certificate_service.Purchase_Certificate 
       add constraint FK44egdemmfljjghun6m9hub8n5 
       foreign key (Id_order) 
       references Certificate_service.Purchase (Id)

    alter table Certificate_service.Tag_Certificate 
       add constraint FKkeg0c012jc7y0y1qpal14sp8w 
       foreign key (IdTag) 
       references Certificate_service.Tag (Id)

    alter table Certificate_service.Tag_Certificate 
       add constraint FKfet3486uu7ptkou8mqtoqkr16 
       foreign key (IdCertificate) 
       references Certificate_service.GiftCertificate (Id)
