-- ao_dbschema.sql
CREATE TABLE IF NOT EXISTS AO_Banks
(
    id    SERIAL PRIMARY KEY ,
    name  VARCHAR(20) NOT NULL
);
CREATE TABLE IF NOT EXISTS AO_currency
(
    id    SERIAL PRIMARY KEY ,
    name  VARCHAR(20) NOT NULL
);
CREATE TABLE IF NOT EXISTS AO_trade
(
    id            SERIAL PRIMARY KEY,
    date_trade    DATE  NOT NULL,
    id_bank       INTEGER NOT NULL,
    name_bank     VARCHAR(20) NOT NULL,
    id_currency   INTEGER NOT NULL,
    name_currency VARCHAR(20) NOT NULL,
    rate_buy       NUMERIC(10,4)  NOT NULL,
    rate_sell      NUMERIC(10,4)  NOT NULL
);
CREATE TABLE IF NOT EXISTS AO_avg_rate
(
    id            SERIAL PRIMARY KEY,
    date_trade    DATE  NOT NULL,
    id_currency   INTEGER NOT NULL,
    name_currency VARCHAR(20) NOT NULL,
    rate_buy       NUMERIC(10,4)  NOT NULL,
    rate_sell      NUMERIC(10,4)  NOT NULL
);
commit;
