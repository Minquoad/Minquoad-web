--
-- PostgreSQL database dump
--

-- Dumped from database version 11.0
-- Dumped by pg_dump version 11.0

-- Started on 2018-11-10 12:41:28

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
-- TOC entry 2830 (class 1262 OID 16393)
-- Name: Minquoad-DB; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE "Minquoad-DB" WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'English_United States.1252' LC_CTYPE = 'English_United States.1252';


ALTER DATABASE "Minquoad-DB" OWNER TO "postgres";

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
-- TOC entry 197 (class 1259 OID 24618)
-- Name: Thing; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE "public"."Thing" (
    "id" integer NOT NULL,
    "description" "text",
    "owner" integer
);


ALTER TABLE "public"."Thing" OWNER TO "postgres";

--
-- TOC entry 196 (class 1259 OID 24616)
-- Name: Thing_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "public"."Thing_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "public"."Thing_id_seq" OWNER TO "postgres";

--
-- TOC entry 2831 (class 0 OID 0)
-- Dependencies: 196
-- Name: Thing_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "public"."Thing_id_seq" OWNED BY "public"."Thing"."id";


--
-- TOC entry 199 (class 1259 OID 24633)
-- Name: User; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE "public"."User" (
    "id" integer NOT NULL,
    "nickname" "text" NOT NULL,
    "hashedSaltedPassword" "text",
    "adminLevel" integer,
    "lastActivityDate" timestamp with time zone,
    "registrationDate" timestamp with time zone,
    "pictureName" "text"
);


ALTER TABLE "public"."User" OWNER TO "postgres";

--
-- TOC entry 198 (class 1259 OID 24631)
-- Name: User_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "public"."User_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "public"."User_id_seq" OWNER TO "postgres";

--
-- TOC entry 2832 (class 0 OID 0)
-- Dependencies: 198
-- Name: User_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "public"."User_id_seq" OWNED BY "public"."User"."id";


--
-- TOC entry 2693 (class 2604 OID 24621)
-- Name: Thing id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "public"."Thing" ALTER COLUMN "id" SET DEFAULT "nextval"('"public"."Thing_id_seq"'::"regclass");


--
-- TOC entry 2694 (class 2604 OID 24636)
-- Name: User id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "public"."User" ALTER COLUMN "id" SET DEFAULT "nextval"('"public"."User_id_seq"'::"regclass");


--
-- TOC entry 2822 (class 0 OID 24618)
-- Dependencies: 197
-- Data for Name: Thing; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO "public"."Thing" ("id", "description", "owner") VALUES (103, 'vcwxwxvcwxcvwxc', NULL);
INSERT INTO "public"."Thing" ("id", "description", "owner") VALUES (105, 'vdvcwvcxvcxw', NULL);
INSERT INTO "public"."Thing" ("id", "description", "owner") VALUES (99, 'zggsfsgdfgfsdgsd', NULL);
INSERT INTO "public"."Thing" ("id", "description", "owner") VALUES (106, 'nbcnbccn', NULL);
INSERT INTO "public"."Thing" ("id", "description", "owner") VALUES (104, 'fdqdsfqsd', NULL);
INSERT INTO "public"."Thing" ("id", "description", "owner") VALUES (100, 'kjhgjhkg', NULL);
INSERT INTO "public"."Thing" ("id", "description", "owner") VALUES (101, 'zearfdqsdsqfdsfqdsfq', NULL);
INSERT INTO "public"."Thing" ("id", "description", "owner") VALUES (102, 'éééééôôôôôô', NULL);
INSERT INTO "public"."Thing" ("id", "description", "owner") VALUES (98, 'à admin mtn', 1);
INSERT INTO "public"."Thing" ("id", "description", "owner") VALUES (107, 'yttreert', NULL);
INSERT INTO "public"."Thing" ("id", "description", "owner") VALUES (90, 'à admin', 1);
INSERT INTO "public"."Thing" ("id", "description", "owner") VALUES (89, 'à admin', 1);


--
-- TOC entry 2824 (class 0 OID 24633)
-- Dependencies: 199
-- Data for Name: User; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO "public"."User" ("id", "nickname", "hashedSaltedPassword", "adminLevel", "lastActivityDate", "registrationDate", "pictureName") VALUES (1, 'Dauchi', 'd164ce7285787838f8aed9b181fbd034115c43bf66b20b256e1ed5a7814a403d', 0, '2018-10-30 22:41:17.766+01', '2018-10-28 19:12:03.669+01', NULL);
INSERT INTO "public"."User" ("id", "nickname", "hashedSaltedPassword", "adminLevel", "lastActivityDate", "registrationDate", "pictureName") VALUES (0, 'Adminquo', '30d6294cf8f5c9ec336827b7dbd7a04bb36330f36641700b5fd1c173fd91c17c', 2147483647, '2018-11-10 12:16:10.044+01', '2018-10-30 22:00:47.192+01', '1207983481536694400');
INSERT INTO "public"."User" ("id", "nickname", "hashedSaltedPassword", "adminLevel", "lastActivityDate", "registrationDate", "pictureName") VALUES (7, 'Adminquo554', '53f69dad9ae5547b4e7e6beed49879949584dab5175c0b54643c66f4737c98aa', 0, '2018-11-06 21:46:15.548+01', '2018-11-06 21:46:09.925+01', NULL);


--
-- TOC entry 2833 (class 0 OID 0)
-- Dependencies: 196
-- Name: Thing_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('"public"."Thing_id_seq"', 107, true);


--
-- TOC entry 2834 (class 0 OID 0)
-- Dependencies: 198
-- Name: User_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('"public"."User_id_seq"', 7, true);


--
-- TOC entry 2696 (class 2606 OID 24626)
-- Name: Thing Thing_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "public"."Thing"
    ADD CONSTRAINT "Thing_pkey" PRIMARY KEY ("id");


--
-- TOC entry 2698 (class 2606 OID 24641)
-- Name: User User_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "public"."User"
    ADD CONSTRAINT "User_pkey" PRIMARY KEY ("id");


--
-- TOC entry 2699 (class 2606 OID 24642)
-- Name: Thing Thing_owner_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "public"."Thing"
    ADD CONSTRAINT "Thing_owner_fkey" FOREIGN KEY ("owner") REFERENCES "public"."User"("id");


-- Completed on 2018-11-10 12:41:28

--
-- PostgreSQL database dump complete
--

