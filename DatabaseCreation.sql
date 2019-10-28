--
-- PostgreSQL database dump
--

-- Dumped from database version 10.7
-- Dumped by pg_dump version 12.0

-- Started on 2019-10-26 16:47:56

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE "Minquoad-DB";
--
-- TOC entry 2906 (class 1262 OID 16393)
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
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 198 (class 1259 OID 16690)
-- Name: Consideration; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Consideration" (
    id bigint NOT NULL,
    "consideringUser" bigint,
    "consideredUser" bigint,
    "perceptionColor" integer,
    status integer
);


--
-- TOC entry 197 (class 1259 OID 16682)
-- Name: Conversation; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Conversation" (
    id bigint NOT NULL,
    title text,
    type integer,
    "lastMessage" bigint
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
    "attemptsNumber" bigint,
    "lastAttemptInstant" timestamp with time zone
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
-- TOC entry 2907 (class 0 OID 0)
-- Dependencies: 207
-- Name: FailedInLoginigAttempt_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public."FailedInLoginigAttempt_id_seq" OWNED BY public."FailedInLoginigAttempt".id;


--
-- TOC entry 210 (class 1259 OID 16831)
-- Name: ImprovementSuggestion; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."ImprovementSuggestion" (
    id bigint NOT NULL,
    text text,
    "user" bigint,
    instant timestamp with time zone
);


--
-- TOC entry 209 (class 1259 OID 16829)
-- Name: ImprovementSuggestion_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public."ImprovementSuggestion_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2908 (class 0 OID 0)
-- Dependencies: 209
-- Name: ImprovementSuggestion_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public."ImprovementSuggestion_id_seq" OWNED BY public."ImprovementSuggestion".id;


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
    conversation bigint,
    "messageFile" bigint
);


--
-- TOC entry 211 (class 1259 OID 16876)
-- Name: MessageFile; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."MessageFile" (
    id bigint NOT NULL,
    image boolean
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
-- TOC entry 2909 (class 0 OID 0)
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
-- TOC entry 2910 (class 0 OID 0)
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
    "defaultColor" integer,
    language text,
    "readabilityImprovementActivated" boolean,
    "typingAssistanceActivated" boolean
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
-- TOC entry 2731 (class 2604 OID 16815)
-- Name: FailedInLoginigAttempt id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."FailedInLoginigAttempt" ALTER COLUMN id SET DEFAULT nextval('public."FailedInLoginigAttempt_id_seq"'::regclass);


--
-- TOC entry 2732 (class 2604 OID 16834)
-- Name: ImprovementSuggestion id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ImprovementSuggestion" ALTER COLUMN id SET DEFAULT nextval('public."ImprovementSuggestion_id_seq"'::regclass);


--
-- TOC entry 2730 (class 2604 OID 16793)
-- Name: RequestLog id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."RequestLog" ALTER COLUMN id SET DEFAULT nextval('public."RequestLog_id_seq"'::regclass);


--
-- TOC entry 2729 (class 2604 OID 16777)
-- Name: Thing id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Thing" ALTER COLUMN id SET DEFAULT nextval('public."Thing_id_seq"'::regclass);


--
-- TOC entry 2741 (class 2606 OID 16697)
-- Name: Consideration Consideration_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Consideration"
    ADD CONSTRAINT "Consideration_pkey" PRIMARY KEY (id);


--
-- TOC entry 2748 (class 2606 OID 16738)
-- Name: ConversationAccess ConversationAccess_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ConversationAccess"
    ADD CONSTRAINT "ConversationAccess_pkey" PRIMARY KEY (id);


--
-- TOC entry 2737 (class 2606 OID 16689)
-- Name: Conversation Conversation_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Conversation"
    ADD CONSTRAINT "Conversation_pkey" PRIMARY KEY (id);


--
-- TOC entry 2760 (class 2606 OID 16820)
-- Name: FailedInLoginigAttempt FailedInLoginigAttempt_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."FailedInLoginigAttempt"
    ADD CONSTRAINT "FailedInLoginigAttempt_pkey" PRIMARY KEY (id);


--
-- TOC entry 2763 (class 2606 OID 16839)
-- Name: ImprovementSuggestion ImprovementSuggestion_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ImprovementSuggestion"
    ADD CONSTRAINT "ImprovementSuggestion_pkey" PRIMARY KEY (id);


--
-- TOC entry 2765 (class 2606 OID 16880)
-- Name: MessageFile MessageFile_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."MessageFile"
    ADD CONSTRAINT "MessageFile_pkey" PRIMARY KEY (id);


--
-- TOC entry 2745 (class 2606 OID 16723)
-- Name: Message Message_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Message"
    ADD CONSTRAINT "Message_pkey" PRIMARY KEY (id);


--
-- TOC entry 2751 (class 2606 OID 16761)
-- Name: ProtectedFile ProtectedFile_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ProtectedFile"
    ADD CONSTRAINT "ProtectedFile_pkey" PRIMARY KEY (id);


--
-- TOC entry 2758 (class 2606 OID 16798)
-- Name: RequestLog RequestLog_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."RequestLog"
    ADD CONSTRAINT "RequestLog_pkey" PRIMARY KEY (id);


--
-- TOC entry 2756 (class 2606 OID 16782)
-- Name: Thing Thing_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Thing"
    ADD CONSTRAINT "Thing_pkey" PRIMARY KEY (id);


--
-- TOC entry 2753 (class 2606 OID 16766)
-- Name: UserProfileImage UserProfileImage_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."UserProfileImage"
    ADD CONSTRAINT "UserProfileImage_pkey" PRIMARY KEY (id);


--
-- TOC entry 2735 (class 2606 OID 16636)
-- Name: User User_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."User"
    ADD CONSTRAINT "User_pkey" PRIMARY KEY (id);


--
-- TOC entry 2738 (class 1259 OID 16827)
-- Name: Consideration_consideringUser_fkey; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "Consideration_consideringUser_fkey" ON public."Consideration" USING btree ("consideringUser");


--
-- TOC entry 2739 (class 1259 OID 16828)
-- Name: Consideration_donsideredUser_fkey; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "Consideration_donsideredUser_fkey" ON public."Consideration" USING btree ("consideredUser");


--
-- TOC entry 2746 (class 1259 OID 16825)
-- Name: ConversationAccess_conversation_fkey; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "ConversationAccess_conversation_fkey" ON public."ConversationAccess" USING btree (conversation);


--
-- TOC entry 2749 (class 1259 OID 16824)
-- Name: ConversationAccess_user_fkey; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "ConversationAccess_user_fkey" ON public."ConversationAccess" USING btree ("user");


--
-- TOC entry 2761 (class 1259 OID 16823)
-- Name: FailedInlogingAttempt_mailAddress; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "FailedInlogingAttempt_mailAddress" ON public."FailedInLoginigAttempt" USING btree ("mailAddress");


--
-- TOC entry 2742 (class 1259 OID 16822)
-- Name: Message_conversation_fkey; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "Message_conversation_fkey" ON public."Message" USING btree (conversation);


--
-- TOC entry 2743 (class 1259 OID 16893)
-- Name: Message_messageFile_fkey; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "Message_messageFile_fkey" ON public."Message" USING btree ("messageFile");


--
-- TOC entry 2754 (class 1259 OID 16826)
-- Name: Thing_owner_fkey; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "Thing_owner_fkey" ON public."Thing" USING btree (owner);


--
-- TOC entry 2733 (class 1259 OID 16821)
-- Name: User_mailAddress; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "User_mailAddress" ON public."User" USING btree ("mailAddress");


--
-- TOC entry 2768 (class 2606 OID 16703)
-- Name: Consideration Consideration_consideredUser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Consideration"
    ADD CONSTRAINT "Consideration_consideredUser_fkey" FOREIGN KEY ("consideredUser") REFERENCES public."User"(id);


--
-- TOC entry 2767 (class 2606 OID 16698)
-- Name: Consideration Consideration_consideringUser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Consideration"
    ADD CONSTRAINT "Consideration_consideringUser_fkey" FOREIGN KEY ("consideringUser") REFERENCES public."User"(id);


--
-- TOC entry 2772 (class 2606 OID 16744)
-- Name: ConversationAccess ConversationAccess_conversation_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ConversationAccess"
    ADD CONSTRAINT "ConversationAccess_conversation_fkey" FOREIGN KEY (conversation) REFERENCES public."Conversation"(id);


--
-- TOC entry 2773 (class 2606 OID 16749)
-- Name: ConversationAccess ConversationAccess_lastSeenMessage_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ConversationAccess"
    ADD CONSTRAINT "ConversationAccess_lastSeenMessage_fkey" FOREIGN KEY ("lastSeenMessage") REFERENCES public."Message"(id);


--
-- TOC entry 2771 (class 2606 OID 16739)
-- Name: ConversationAccess ConversationAccess_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ConversationAccess"
    ADD CONSTRAINT "ConversationAccess_user_fkey" FOREIGN KEY ("user") REFERENCES public."User"(id);


--
-- TOC entry 2766 (class 2606 OID 16899)
-- Name: Conversation Conversation_lastMessage_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Conversation"
    ADD CONSTRAINT "Conversation_lastMessage_fkey" FOREIGN KEY ("lastMessage") REFERENCES public."Message"(id);


--
-- TOC entry 2778 (class 2606 OID 16840)
-- Name: ImprovementSuggestion ImprovementSuggestion_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ImprovementSuggestion"
    ADD CONSTRAINT "ImprovementSuggestion_user_fkey" FOREIGN KEY ("user") REFERENCES public."User"(id);


--
-- TOC entry 2779 (class 2606 OID 16881)
-- Name: MessageFile MessageFile_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."MessageFile"
    ADD CONSTRAINT "MessageFile_id_fkey" FOREIGN KEY (id) REFERENCES public."ProtectedFile"(id);


--
-- TOC entry 2770 (class 2606 OID 16729)
-- Name: Message Message_conversation_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Message"
    ADD CONSTRAINT "Message_conversation_fkey" FOREIGN KEY (conversation) REFERENCES public."Conversation"(id);


--
-- TOC entry 2769 (class 2606 OID 16724)
-- Name: Message Message_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Message"
    ADD CONSTRAINT "Message_user_fkey" FOREIGN KEY ("user") REFERENCES public."User"(id);


--
-- TOC entry 2777 (class 2606 OID 16804)
-- Name: RequestLog RequestLog_controllingAdmin_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."RequestLog"
    ADD CONSTRAINT "RequestLog_controllingAdmin_fkey" FOREIGN KEY ("controllingAdmin") REFERENCES public."User"(id);


--
-- TOC entry 2776 (class 2606 OID 16799)
-- Name: RequestLog RequestLog_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."RequestLog"
    ADD CONSTRAINT "RequestLog_user_fkey" FOREIGN KEY ("user") REFERENCES public."User"(id);


--
-- TOC entry 2775 (class 2606 OID 16783)
-- Name: Thing Thing_owner_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Thing"
    ADD CONSTRAINT "Thing_owner_fkey" FOREIGN KEY (owner) REFERENCES public."User"(id);


--
-- TOC entry 2774 (class 2606 OID 16767)
-- Name: UserProfileImage UserProfileImage_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."UserProfileImage"
    ADD CONSTRAINT "UserProfileImage_user_fkey" FOREIGN KEY ("user") REFERENCES public."User"(id);


-- Completed on 2019-10-26 16:47:56

--
-- PostgreSQL database dump complete
--

