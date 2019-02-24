--
-- PostgreSQL database dump
--

-- Dumped from database version 10.7
-- Dumped by pg_dump version 10.7

-- Started on 2019-02-21 00:48:46

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE "Minquoad-DB";
--
-- TOC entry 2811 (class 1262 OID 16393)
-- Name: Minquoad-DB; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE "Minquoad-DB" WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'French_France.1252' LC_CTYPE = 'French_France.1252';


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

--
-- TOC entry 1 (class 3079 OID 12924)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2814 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 199 (class 1259 OID 16407)
-- Name: Thing; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Thing" (
    id integer NOT NULL,
    description text,
    owner integer
);


ALTER TABLE public."Thing" OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 16405)
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
-- TOC entry 2815 (class 0 OID 0)
-- Dependencies: 198
-- Name: Thing_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Thing_id_seq" OWNED BY public."Thing".id;


--
-- TOC entry 197 (class 1259 OID 16396)
-- Name: User; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."User" (
    id integer NOT NULL,
    "hashedSaltedPassword" text,
    "pictureName" text,
    "registrationDate" timestamp with time zone,
    "lastActivityDate" timestamp with time zone,
    "adminLevel" integer,
    "unblockDate" timestamp with time zone,
    nickname text
);


ALTER TABLE public."User" OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 16394)
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
-- TOC entry 2816 (class 0 OID 0)
-- Dependencies: 196
-- Name: User_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."User_id_seq" OWNED BY public."User".id;


--
-- TOC entry 2679 (class 2604 OID 16410)
-- Name: Thing id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Thing" ALTER COLUMN id SET DEFAULT nextval('public."Thing_id_seq"'::regclass);


--
-- TOC entry 2678 (class 2604 OID 16399)
-- Name: User id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."User" ALTER COLUMN id SET DEFAULT nextval('public."User_id_seq"'::regclass);


--
-- TOC entry 2683 (class 2606 OID 16415)
-- Name: Thing Thing_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Thing"
    ADD CONSTRAINT "Thing_pkey" PRIMARY KEY (id);


--
-- TOC entry 2681 (class 2606 OID 16404)
-- Name: User User_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."User"
    ADD CONSTRAINT "User_pkey" PRIMARY KEY (id);


--
-- TOC entry 2684 (class 2606 OID 16416)
-- Name: Thing Thing_owner_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Thing"
    ADD CONSTRAINT "Thing_owner_fkey" FOREIGN KEY (owner) REFERENCES public."User"(id) ON DELETE CASCADE;


--
-- TOC entry 2813 (class 0 OID 0)
-- Dependencies: 6
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2019-02-21 00:48:46

--
-- PostgreSQL database dump complete
--

