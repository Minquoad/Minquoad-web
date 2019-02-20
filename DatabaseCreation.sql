--
-- PostgreSQL database dump
--

-- Dumped from database version 11.1
-- Dumped by pg_dump version 11.1

-- Started on 2019-02-20 21:14:06

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2180 (class 1262 OID 16384)
-- Name: Minquoad-DB; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE "Minquoad-DB" WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'English_United States.1252' LC_CTYPE = 'English_United States.1252';


ALTER DATABASE "Minquoad-DB" OWNER TO postgres;

\connect -reuse-previous=on "dbname='Minquoad-DB'"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 196 (class 1259 OID 16385)
-- Name: Thing; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Thing" (
    id integer NOT NULL,
    description text,
    owner integer
);


ALTER TABLE public."Thing" OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 16391)
-- Name: Thing_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Thing_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."Thing_id_seq" OWNER TO postgres;

--
-- TOC entry 2181 (class 0 OID 0)
-- Dependencies: 197
-- Name: Thing_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Thing_id_seq" OWNED BY public."Thing".id;


--
-- TOC entry 198 (class 1259 OID 16393)
-- Name: User; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."User" (
    id integer NOT NULL,
    nickname text NOT NULL,
    "hashedSaltedPassword" text,
    "adminLevel" integer,
    "lastActivityDate" timestamp with time zone,
    "registrationDate" timestamp with time zone,
    "pictureName" text,
    "unblockDate" time with time zone
);


ALTER TABLE public."User" OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 16399)
-- Name: User_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."User_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."User_id_seq" OWNER TO postgres;

--
-- TOC entry 2182 (class 0 OID 0)
-- Dependencies: 199
-- Name: User_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."User_id_seq" OWNED BY public."User".id;


--
-- TOC entry 2047 (class 2604 OID 16401)
-- Name: Thing id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Thing" ALTER COLUMN id SET DEFAULT nextval('public."Thing_id_seq"'::regclass);


--
-- TOC entry 2048 (class 2604 OID 16402)
-- Name: User id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."User" ALTER COLUMN id SET DEFAULT nextval('public."User_id_seq"'::regclass);


--
-- TOC entry 2050 (class 2606 OID 16404)
-- Name: Thing Thing_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Thing"
    ADD CONSTRAINT "Thing_pkey" PRIMARY KEY (id);


--
-- TOC entry 2052 (class 2606 OID 16406)
-- Name: User User_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."User"
    ADD CONSTRAINT "User_pkey" PRIMARY KEY (id);


--
-- TOC entry 2053 (class 2606 OID 16407)
-- Name: Thing Thing_owner_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Thing"
    ADD CONSTRAINT "Thing_owner_fkey" FOREIGN KEY (owner) REFERENCES public."User"(id);


-- Completed on 2019-02-20 21:14:06

--
-- PostgreSQL database dump complete
--

