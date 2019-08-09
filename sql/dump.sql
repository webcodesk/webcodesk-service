--
-- PostgreSQL database dump
--

-- Dumped from database version 11.1
-- Dumped by pg_dump version 11.1

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
-- Name: account_license; Type: TABLE; Schema: public; Owner: webcodesk_site
--

CREATE TABLE public.account_license (
    id bigint NOT NULL,
    account_id bigint NOT NULL,
    start_date timestamp without time zone NOT NULL,
    end_date timestamp without time zone NOT NULL,
    license_id bigint NOT NULL
);


ALTER TABLE public.account_license OWNER TO webcodesk_site;

--
-- Name: account_license_activation; Type: TABLE; Schema: public; Owner: webcodesk_site
--

CREATE TABLE public.account_license_activation (
    id bigint NOT NULL,
    account_license_id bigint NOT NULL,
    status character varying NOT NULL,
    activate_date timestamp without time zone NOT NULL,
    deactivate_date timestamp without time zone
);


ALTER TABLE public.account_license_activation OWNER TO webcodesk_site;

--
-- Name: account_license_activation_id_seq; Type: SEQUENCE; Schema: public; Owner: webcodesk_site
--

CREATE SEQUENCE public.account_license_activation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.account_license_activation_id_seq OWNER TO webcodesk_site;

--
-- Name: account_license_activation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: webcodesk_site
--

ALTER SEQUENCE public.account_license_activation_id_seq OWNED BY public.account_license_activation.id;


--
-- Name: account_license_id_seq; Type: SEQUENCE; Schema: public; Owner: webcodesk_site
--

CREATE SEQUENCE public.account_license_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.account_license_id_seq OWNER TO webcodesk_site;

--
-- Name: account_license_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: webcodesk_site
--

ALTER SEQUENCE public.account_license_id_seq OWNED BY public.account_license.id;


--
-- Name: application_version; Type: TABLE; Schema: public; Owner: webcodesk_site
--

CREATE TABLE public.application_version (
    id bigint NOT NULL,
    version character varying NOT NULL,
    create_date timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.application_version OWNER TO webcodesk_site;

--
-- Name: application_version_id_seq; Type: SEQUENCE; Schema: public; Owner: webcodesk_site
--

CREATE SEQUENCE public.application_version_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.application_version_id_seq OWNER TO webcodesk_site;

--
-- Name: application_version_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: webcodesk_site
--

ALTER SEQUENCE public.application_version_id_seq OWNED BY public.application_version.id;


--
-- Name: license; Type: TABLE; Schema: public; Owner: webcodesk_site
--

CREATE TABLE public.license (
    id bigint NOT NULL,
    name character varying NOT NULL,
    duration integer NOT NULL,
    price bigint NOT NULL
);


ALTER TABLE public.license OWNER TO webcodesk_site;

--
-- Name: license_id_seq; Type: SEQUENCE; Schema: public; Owner: webcodesk_site
--

CREATE SEQUENCE public.license_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.license_id_seq OWNER TO webcodesk_site;

--
-- Name: license_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: webcodesk_site
--

ALTER SEQUENCE public.license_id_seq OWNED BY public.license.id;


--
-- Name: market_component_id_seq; Type: SEQUENCE; Schema: public; Owner: webcodesk_site
--

CREATE SEQUENCE public.market_component_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.market_component_id_seq OWNER TO webcodesk_site;

--
-- Name: market_component; Type: TABLE; Schema: public; Owner: webcodesk_site
--

CREATE TABLE public.market_component (
    id bigint DEFAULT nextval('public.market_component_id_seq'::regclass) NOT NULL,
    project_id bigint NOT NULL,
    "group" character varying NOT NULL,
    description character varying NOT NULL,
    tags character varying NOT NULL,
    download_count integer DEFAULT 0 NOT NULL,
    name character varying NOT NULL,
    create_date timestamp without time zone DEFAULT now() NOT NULL,
    update_date timestamp without time zone DEFAULT now() NOT NULL,
    lang character varying DEFAULT 'js'::character varying NOT NULL,
    type character varying DEFAULT 'component'::character varying NOT NULL,
    tags_tokens tsvector NOT NULL
);


ALTER TABLE public.market_component OWNER TO webcodesk_site;

--
-- Name: market_project_id_seq; Type: SEQUENCE; Schema: public; Owner: webcodesk_site
--

CREATE SEQUENCE public.market_project_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.market_project_id_seq OWNER TO webcodesk_site;

--
-- Name: market_project; Type: TABLE; Schema: public; Owner: webcodesk_site
--

CREATE TABLE public.market_project (
    id bigint DEFAULT nextval('public.market_project_id_seq'::regclass) NOT NULL,
    name character varying(300) NOT NULL,
    account_id bigint NOT NULL,
    repo_url character varying,
    demo_url character varying,
    create_date timestamp without time zone DEFAULT now() NOT NULL,
    license character varying,
    status integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.market_project OWNER TO webcodesk_site;

--
-- Name: user_account; Type: TABLE; Schema: public; Owner: webcodesk_site
--

CREATE TABLE public.user_account (
    id bigint NOT NULL,
    email character varying(200) NOT NULL,
    password character varying NOT NULL,
    authorities character varying,
    create_date timestamp without time zone DEFAULT now() NOT NULL,
    first_name character varying NOT NULL,
    last_name character varying NOT NULL
);


ALTER TABLE public.user_account OWNER TO webcodesk_site;

--
-- Name: user_account_id_seq; Type: SEQUENCE; Schema: public; Owner: webcodesk_site
--

CREATE SEQUENCE public.user_account_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_account_id_seq OWNER TO webcodesk_site;

--
-- Name: user_account_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: webcodesk_site
--

ALTER SEQUENCE public.user_account_id_seq OWNED BY public.user_account.id;


--
-- Name: user_registration; Type: TABLE; Schema: public; Owner: webcodesk_site
--

CREATE TABLE public.user_registration (
    id bigint NOT NULL,
    email character varying NOT NULL,
    record_id character varying NOT NULL,
    create_date timestamp without time zone DEFAULT now() NOT NULL,
    type character varying NOT NULL
);


ALTER TABLE public.user_registration OWNER TO webcodesk_site;

--
-- Name: user_registration_id_seq; Type: SEQUENCE; Schema: public; Owner: webcodesk_site
--

CREATE SEQUENCE public.user_registration_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_registration_id_seq OWNER TO webcodesk_site;

--
-- Name: user_registration_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: webcodesk_site
--

ALTER SEQUENCE public.user_registration_id_seq OWNED BY public.user_registration.id;


--
-- Name: account_license id; Type: DEFAULT; Schema: public; Owner: webcodesk_site
--

ALTER TABLE ONLY public.account_license ALTER COLUMN id SET DEFAULT nextval('public.account_license_id_seq'::regclass);


--
-- Name: account_license_activation id; Type: DEFAULT; Schema: public; Owner: webcodesk_site
--

ALTER TABLE ONLY public.account_license_activation ALTER COLUMN id SET DEFAULT nextval('public.account_license_activation_id_seq'::regclass);


--
-- Name: application_version id; Type: DEFAULT; Schema: public; Owner: webcodesk_site
--

ALTER TABLE ONLY public.application_version ALTER COLUMN id SET DEFAULT nextval('public.application_version_id_seq'::regclass);


--
-- Name: license id; Type: DEFAULT; Schema: public; Owner: webcodesk_site
--

ALTER TABLE ONLY public.license ALTER COLUMN id SET DEFAULT nextval('public.license_id_seq'::regclass);


--
-- Name: user_account id; Type: DEFAULT; Schema: public; Owner: webcodesk_site
--

ALTER TABLE ONLY public.user_account ALTER COLUMN id SET DEFAULT nextval('public.user_account_id_seq'::regclass);


--
-- Name: user_registration id; Type: DEFAULT; Schema: public; Owner: webcodesk_site
--

ALTER TABLE ONLY public.user_registration ALTER COLUMN id SET DEFAULT nextval('public.user_registration_id_seq'::regclass);


--
-- Data for Name: account_license; Type: TABLE DATA; Schema: public; Owner: webcodesk_site
--



--
-- Data for Name: account_license_activation; Type: TABLE DATA; Schema: public; Owner: webcodesk_site
--



--
-- Data for Name: application_version; Type: TABLE DATA; Schema: public; Owner: webcodesk_site
--

INSERT INTO public.application_version VALUES (1, '1.0.0-beta.1', '2019-02-05 14:09:22.418333');
INSERT INTO public.application_version VALUES (4, '1.0.0-beta.2', '2019-02-28 13:55:27.171344');
INSERT INTO public.application_version VALUES (5, '1.0.0-beta.3', '2019-04-13 18:12:32.744675');
INSERT INTO public.application_version VALUES (6, '1.0.0-beta.4', '2019-04-18 09:59:04.005538');
INSERT INTO public.application_version VALUES (7, '1.0.0', '2019-04-25 16:34:40.170357');


--
-- Data for Name: license; Type: TABLE DATA; Schema: public; Owner: webcodesk_site
--

INSERT INTO public.license VALUES (1, 'Trial Pro', 60, 0);


--
-- Data for Name: market_component; Type: TABLE DATA; Schema: public; Owner: webcodesk_site
--



--
-- Data for Name: market_project; Type: TABLE DATA; Schema: public; Owner: webcodesk_site
--



--
-- Data for Name: user_account; Type: TABLE DATA; Schema: public; Owner: webcodesk_site
--



--
-- Data for Name: user_registration; Type: TABLE DATA; Schema: public; Owner: webcodesk_site
--



--
-- Name: account_license_activation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webcodesk_site
--

SELECT pg_catalog.setval('public.account_license_activation_id_seq', 63, true);


--
-- Name: account_license_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webcodesk_site
--

SELECT pg_catalog.setval('public.account_license_id_seq', 125, true);


--
-- Name: application_version_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webcodesk_site
--

SELECT pg_catalog.setval('public.application_version_id_seq', 7, true);


--
-- Name: license_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webcodesk_site
--

SELECT pg_catalog.setval('public.license_id_seq', 1, true);


--
-- Name: market_component_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webcodesk_site
--

SELECT pg_catalog.setval('public.market_component_id_seq', 24, true);


--
-- Name: market_project_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webcodesk_site
--

SELECT pg_catalog.setval('public.market_project_id_seq', 7, true);


--
-- Name: user_account_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webcodesk_site
--

SELECT pg_catalog.setval('public.user_account_id_seq', 160, true);


--
-- Name: user_registration_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webcodesk_site
--

SELECT pg_catalog.setval('public.user_registration_id_seq', 241, true);


--
-- Name: account_license_activation account_license_activation_pk; Type: CONSTRAINT; Schema: public; Owner: webcodesk_site
--

ALTER TABLE ONLY public.account_license_activation
    ADD CONSTRAINT account_license_activation_pk PRIMARY KEY (id);


--
-- Name: account_license account_license_pk; Type: CONSTRAINT; Schema: public; Owner: webcodesk_site
--

ALTER TABLE ONLY public.account_license
    ADD CONSTRAINT account_license_pk PRIMARY KEY (id);


--
-- Name: application_version application_version_pk; Type: CONSTRAINT; Schema: public; Owner: webcodesk_site
--

ALTER TABLE ONLY public.application_version
    ADD CONSTRAINT application_version_pk PRIMARY KEY (id);


--
-- Name: license license_pk; Type: CONSTRAINT; Schema: public; Owner: webcodesk_site
--

ALTER TABLE ONLY public.license
    ADD CONSTRAINT license_pk PRIMARY KEY (id);


--
-- Name: market_component market_component_pk; Type: CONSTRAINT; Schema: public; Owner: webcodesk_site
--

ALTER TABLE ONLY public.market_component
    ADD CONSTRAINT market_component_pk PRIMARY KEY (id);


--
-- Name: market_project market_project_pk; Type: CONSTRAINT; Schema: public; Owner: webcodesk_site
--

ALTER TABLE ONLY public.market_project
    ADD CONSTRAINT market_project_pk PRIMARY KEY (id);


--
-- Name: user_account user_account_pkey; Type: CONSTRAINT; Schema: public; Owner: webcodesk_site
--

ALTER TABLE ONLY public.user_account
    ADD CONSTRAINT user_account_pkey PRIMARY KEY (id);


--
-- Name: user_registration user_registration_pkey; Type: CONSTRAINT; Schema: public; Owner: webcodesk_site
--

ALTER TABLE ONLY public.user_registration
    ADD CONSTRAINT user_registration_pkey PRIMARY KEY (id);


--
-- Name: account_license_activation_id_uindex; Type: INDEX; Schema: public; Owner: webcodesk_site
--

CREATE UNIQUE INDEX account_license_activation_id_uindex ON public.account_license_activation USING btree (id);


--
-- Name: account_license_id_uindex; Type: INDEX; Schema: public; Owner: webcodesk_site
--

CREATE UNIQUE INDEX account_license_id_uindex ON public.account_license USING btree (id);


--
-- Name: application_version_id_uindex; Type: INDEX; Schema: public; Owner: webcodesk_site
--

CREATE UNIQUE INDEX application_version_id_uindex ON public.application_version USING btree (id);


--
-- Name: application_version_version_uindex; Type: INDEX; Schema: public; Owner: webcodesk_site
--

CREATE UNIQUE INDEX application_version_version_uindex ON public.application_version USING btree (version);


--
-- Name: license_id_uindex; Type: INDEX; Schema: public; Owner: webcodesk_site
--

CREATE UNIQUE INDEX license_id_uindex ON public.license USING btree (id);


--
-- Name: market_component_id_uindex; Type: INDEX; Schema: public; Owner: webcodesk_site
--

CREATE UNIQUE INDEX market_component_id_uindex ON public.market_component USING btree (id);


--
-- Name: market_component_project_id_group_name_uindex; Type: INDEX; Schema: public; Owner: webcodesk_site
--

CREATE UNIQUE INDEX market_component_project_id_group_name_uindex ON public.market_component USING btree (project_id, "group", name);


--
-- Name: market_project_account_id_name_uindex; Type: INDEX; Schema: public; Owner: webcodesk_site
--

CREATE UNIQUE INDEX market_project_account_id_name_uindex ON public.market_project USING btree (account_id, name);


--
-- Name: market_project_id_uindex; Type: INDEX; Schema: public; Owner: webcodesk_site
--

CREATE UNIQUE INDEX market_project_id_uindex ON public.market_project USING btree (id);


--
-- Name: user_account_id_uindex; Type: INDEX; Schema: public; Owner: webcodesk_site
--

CREATE UNIQUE INDEX user_account_id_uindex ON public.user_account USING btree (id);


--
-- Name: user_account_user_name_uindex; Type: INDEX; Schema: public; Owner: webcodesk_site
--

CREATE UNIQUE INDEX user_account_user_name_uindex ON public.user_account USING btree (email);


--
-- Name: user_registration_email_index; Type: INDEX; Schema: public; Owner: webcodesk_site
--

CREATE INDEX user_registration_email_index ON public.user_registration USING btree (email);


--
-- Name: user_registration_id_uindex; Type: INDEX; Schema: public; Owner: webcodesk_site
--

CREATE UNIQUE INDEX user_registration_id_uindex ON public.user_registration USING btree (id);


--
-- Name: user_registration_uuid_index; Type: INDEX; Schema: public; Owner: webcodesk_site
--

CREATE INDEX user_registration_uuid_index ON public.user_registration USING btree (record_id);


--
-- Name: account_license_activation account_license_activation_account_license_fk; Type: FK CONSTRAINT; Schema: public; Owner: webcodesk_site
--

ALTER TABLE ONLY public.account_license_activation
    ADD CONSTRAINT account_license_activation_account_license_fk FOREIGN KEY (account_license_id) REFERENCES public.account_license(id);


--
-- Name: account_license account_license_license_fk; Type: FK CONSTRAINT; Schema: public; Owner: webcodesk_site
--

ALTER TABLE ONLY public.account_license
    ADD CONSTRAINT account_license_license_fk FOREIGN KEY (license_id) REFERENCES public.license(id);


--
-- Name: account_license account_license_user_account_fk; Type: FK CONSTRAINT; Schema: public; Owner: webcodesk_site
--

ALTER TABLE ONLY public.account_license
    ADD CONSTRAINT account_license_user_account_fk FOREIGN KEY (account_id) REFERENCES public.user_account(id);


--
-- Name: market_component market_component_market_project_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: webcodesk_site
--

ALTER TABLE ONLY public.market_component
    ADD CONSTRAINT market_component_market_project_id_fk FOREIGN KEY (project_id) REFERENCES public.market_project(id) ON DELETE CASCADE;


--
-- Name: market_project market_project_user_account_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: webcodesk_site
--

ALTER TABLE ONLY public.market_project
    ADD CONSTRAINT market_project_user_account_id_fk FOREIGN KEY (account_id) REFERENCES public.user_account(id);


--
-- PostgreSQL database dump complete
--

