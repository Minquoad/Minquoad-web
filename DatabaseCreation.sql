--
-- PostgreSQL database dump
--

-- Dumped from database version 10.7
-- Dumped by pg_dump version 10.7

-- Started on 2019-04-07 16:17:16

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
-- TOC entry 2886 (class 1262 OID 16393)
-- Name: Minquoad-DB; Type: DATABASE; Schema: -; Owner: -
--

CREATE DATABASE "Minquoad-DB" WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'French_France.1252' LC_CTYPE = 'French_France.1252';


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
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2888 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 198 (class 1259 OID 16690)
-- Name: Consideration; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Consideration" (
    id bigint NOT NULL,
    "consideringUser" bigint,
    "consideredUser" bigint,
    "statuString" text,
    "perceptionColor" integer
);


--
-- TOC entry 197 (class 1259 OID 16682)
-- Name: Conversation; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Conversation" (
    id bigint NOT NULL,
    title text,
    type integer
);


--
-- TOC entry 200 (class 1259 OID 16734)
-- Name: ConversationAccess; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."ConversationAccess" (
    id bigint NOT NULL,
    "user" bigint,
    conversation bigint,
    administrator boolean,
    "lastSeenMessage" bigint
);


--
-- TOC entry 208 (class 1259 OID 16812)
-- Name: FailedInLoginigAttempt; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."FailedInLoginigAttempt" (
    id bigint NOT NULL,
    "mailAddress" text,
    "attemptsCount" bigint,
    "lastArremptInstant" timestamp with time zone
);


--
-- TOC entry 207 (class 1259 OID 16810)
-- Name: FailedInLoginigAttempt_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public."FailedInLoginigAttempt_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2889 (class 0 OID 0)
-- Dependencies: 207
-- Name: FailedInLoginigAttempt_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public."FailedInLoginigAttempt_id_seq" OWNED BY public."FailedInLoginigAttempt".id;


--
-- TOC entry 199 (class 1259 OID 16716)
-- Name: Message; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Message" (
    id bigint NOT NULL,
    text text,
    "editedText" text,
    instant timestamp with time zone,
    "user" bigint,
    conversation bigint
);


--
-- TOC entry 201 (class 1259 OID 16754)
-- Name: ProtectedFile; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."ProtectedFile" (
    id bigint NOT NULL,
    "relativePath" text,
    "originalName" text
);


--
-- TOC entry 206 (class 1259 OID 16790)
-- Name: RequestLog; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."RequestLog" (
    id bigint NOT NULL,
    instant timestamp with time zone,
    url text,
    "user" bigint,
    "controllingAdmin" bigint,
    "ipAddress" text,
    error text,
    "serviceDuration" integer,
    "servletName" text
);


--
-- TOC entry 205 (class 1259 OID 16788)
-- Name: RequestLog_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public."RequestLog_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2890 (class 0 OID 0)
-- Dependencies: 205
-- Name: RequestLog_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public."RequestLog_id_seq" OWNED BY public."RequestLog".id;


--
-- TOC entry 204 (class 1259 OID 16774)
-- Name: Thing; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Thing" (
    id integer NOT NULL,
    owner bigint,
    description text
);


--
-- TOC entry 203 (class 1259 OID 16772)
-- Name: Thing_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public."Thing_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2891 (class 0 OID 0)
-- Dependencies: 203
-- Name: Thing_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public."Thing_id_seq" OWNED BY public."Thing".id;


--
-- TOC entry 196 (class 1259 OID 16629)
-- Name: User; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."User" (
    id bigint NOT NULL,
    "mailAddress" text,
    nickname text,
    "hashedSaltedPassword" text,
    "registrationInstant" timestamp with time zone,
    "lastActivityInstant" timestamp with time zone,
    "adminLevel" integer,
    "unblockInstant" timestamp with time zone,
    "defaultColor" integer
);


--
-- TOC entry 202 (class 1259 OID 16762)
-- Name: UserProfileImage; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."UserProfileImage" (
    id bigint NOT NULL,
    "user" bigint
);


--
-- TOC entry 2720 (class 2604 OID 16815)
-- Name: FailedInLoginigAttempt id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."FailedInLoginigAttempt" ALTER COLUMN id SET DEFAULT nextval('public."FailedInLoginigAttempt_id_seq"'::regclass);


--
-- TOC entry 2719 (class 2604 OID 16793)
-- Name: RequestLog id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."RequestLog" ALTER COLUMN id SET DEFAULT nextval('public."RequestLog_id_seq"'::regclass);


--
-- TOC entry 2718 (class 2604 OID 16777)
-- Name: Thing id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Thing" ALTER COLUMN id SET DEFAULT nextval('public."Thing_id_seq"'::regclass);


--
-- TOC entry 2729 (class 2606 OID 16697)
-- Name: Consideration Consideration_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Consideration"
    ADD CONSTRAINT "Consideration_pkey" PRIMARY KEY (id);


--
-- TOC entry 2735 (class 2606 OID 16738)
-- Name: ConversationAccess ConversationAccess_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ConversationAccess"
    ADD CONSTRAINT "ConversationAccess_pkey" PRIMARY KEY (id);


--
-- TOC entry 2725 (class 2606 OID 16689)
-- Name: Conversation Conversation_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Conversation"
    ADD CONSTRAINT "Conversation_pkey" PRIMARY KEY (id);


--
-- TOC entry 2747 (class 2606 OID 16820)
-- Name: FailedInLoginigAttempt FailedInLoginigAttempt_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."FailedInLoginigAttempt"
    ADD CONSTRAINT "FailedInLoginigAttempt_pkey" PRIMARY KEY (id);


--
-- TOC entry 2732 (class 2606 OID 16723)
-- Name: Message Message_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Message"
    ADD CONSTRAINT "Message_pkey" PRIMARY KEY (id);


--
-- TOC entry 2738 (class 2606 OID 16761)
-- Name: ProtectedFile ProtectedFile_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ProtectedFile"
    ADD CONSTRAINT "ProtectedFile_pkey" PRIMARY KEY (id);


--
-- TOC entry 2745 (class 2606 OID 16798)
-- Name: RequestLog RequestLog_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."RequestLog"
    ADD CONSTRAINT "RequestLog_pkey" PRIMARY KEY (id);


--
-- TOC entry 2743 (class 2606 OID 16782)
-- Name: Thing Thing_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Thing"
    ADD CONSTRAINT "Thing_pkey" PRIMARY KEY (id);


--
-- TOC entry 2740 (class 2606 OID 16766)
-- Name: UserProfileImage UserProfileImage_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."UserProfileImage"
    ADD CONSTRAINT "UserProfileImage_pkey" PRIMARY KEY (id);


--
-- TOC entry 2723 (class 2606 OID 16636)
-- Name: User User_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."User"
    ADD CONSTRAINT "User_pkey" PRIMARY KEY (id);


--
-- TOC entry 2726 (class 1259 OID 16827)
-- Name: Consideration_consideringUser_fkey; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "Consideration_consideringUser_fkey" ON public."Consideration" USING btree ("consideringUser");


--
-- TOC entry 2727 (class 1259 OID 16828)
-- Name: Consideration_donsideredUser_fkey; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "Consideration_donsideredUser_fkey" ON public."Consideration" USING btree ("consideredUser");


--
-- TOC entry 2733 (class 1259 OID 16825)
-- Name: ConversationAccess_conversation_fkey; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "ConversationAccess_conversation_fkey" ON public."ConversationAccess" USING btree (conversation);


--
-- TOC entry 2736 (class 1259 OID 16824)
-- Name: ConversationAccess_user_fkey; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "ConversationAccess_user_fkey" ON public."ConversationAccess" USING btree ("user");


--
-- TOC entry 2748 (class 1259 OID 16823)
-- Name: FailedInlogingAttempt_mailAddress; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "FailedInlogingAttempt_mailAddress" ON public."FailedInLoginigAttempt" USING btree ("mailAddress");


--
-- TOC entry 2730 (class 1259 OID 16822)
-- Name: Message_conversation_fkey; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "Message_conversation_fkey" ON public."Message" USING btree (conversation);


--
-- TOC entry 2741 (class 1259 OID 16826)
-- Name: Thing_owner_fkey; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "Thing_owner_fkey" ON public."Thing" USING btree (owner);


--
-- TOC entry 2721 (class 1259 OID 16821)
-- Name: User_mailAddress; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "User_mailAddress" ON public."User" USING btree ("mailAddress");


--
-- TOC entry 2750 (class 2606 OID 16703)
-- Name: Consideration Consideration_consideredUser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Consideration"
    ADD CONSTRAINT "Consideration_consideredUser_fkey" FOREIGN KEY ("consideredUser") REFERENCES public."User"(id);


--
-- TOC entry 2749 (class 2606 OID 16698)
-- Name: Consideration Consideration_consideringUser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Consideration"
    ADD CONSTRAINT "Consideration_consideringUser_fkey" FOREIGN KEY ("consideringUser") REFERENCES public."User"(id);


--
-- TOC entry 2754 (class 2606 OID 16744)
-- Name: ConversationAccess ConversationAccess_conversation_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ConversationAccess"
    ADD CONSTRAINT "ConversationAccess_conversation_fkey" FOREIGN KEY (conversation) REFERENCES public."Conversation"(id);


--
-- TOC entry 2755 (class 2606 OID 16749)
-- Name: ConversationAccess ConversationAccess_lastSeenMessage_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ConversationAccess"
    ADD CONSTRAINT "ConversationAccess_lastSeenMessage_fkey" FOREIGN KEY ("lastSeenMessage") REFERENCES public."Message"(id);


--
-- TOC entry 2753 (class 2606 OID 16739)
-- Name: ConversationAccess ConversationAccess_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ConversationAccess"
    ADD CONSTRAINT "ConversationAccess_user_fkey" FOREIGN KEY ("user") REFERENCES public."User"(id);


--
-- TOC entry 2752 (class 2606 OID 16729)
-- Name: Message Message_conversation_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Message"
    ADD CONSTRAINT "Message_conversation_fkey" FOREIGN KEY (conversation) REFERENCES public."Conversation"(id);


--
-- TOC entry 2751 (class 2606 OID 16724)
-- Name: Message Message_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Message"
    ADD CONSTRAINT "Message_user_fkey" FOREIGN KEY ("user") REFERENCES public."User"(id);


--
-- TOC entry 2759 (class 2606 OID 16804)
-- Name: RequestLog RequestLog_controllingAdmin_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."RequestLog"
    ADD CONSTRAINT "RequestLog_controllingAdmin_fkey" FOREIGN KEY ("controllingAdmin") REFERENCES public."User"(id);


--
-- TOC entry 2758 (class 2606 OID 16799)
-- Name: RequestLog RequestLog_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."RequestLog"
    ADD CONSTRAINT "RequestLog_user_fkey" FOREIGN KEY ("user") REFERENCES public."User"(id);


--
-- TOC entry 2757 (class 2606 OID 16783)
-- Name: Thing Thing_owner_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Thing"
    ADD CONSTRAINT "Thing_owner_fkey" FOREIGN KEY (owner) REFERENCES public."User"(id);


--
-- TOC entry 2756 (class 2606 OID 16767)
-- Name: UserProfileImage UserProfileImage_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."UserProfileImage"
    ADD CONSTRAINT "UserProfileImage_user_fkey" FOREIGN KEY ("user") REFERENCES public."User"(id);


-- Completed on 2019-04-07 16:17:16

--
-- PostgreSQL database dump complete
--

