-- Initial CareNexus schema, matching the existing PostgreSQL schema dump.
-- Empty schemas execute V1. Existing populated schemas must be baselined at version 1.

CREATE TABLE public.patients (
    patient_id character varying(255) NOT NULL,
    age integer NOT NULL,
    insurance_status boolean NOT NULL,
    patient_name character varying(255) NOT NULL,
    phone_number character varying(255) NOT NULL
);

CREATE TABLE public.doctors (
    doctor_id character varying(255) NOT NULL,
    consultation_fee numeric(10,2) NOT NULL,
    doctor_name character varying(255) NOT NULL,
    specialization character varying(255) NOT NULL
);

CREATE TABLE public.appointments (
    appointment_id character varying(255) NOT NULL,
    appointment_date date NOT NULL,
    status character varying(20) NOT NULL,
    doctor_id character varying(255) NOT NULL,
    patient_id character varying(255) NOT NULL,
    CONSTRAINT appointments_status_check CHECK (((status)::text = ANY ((ARRAY['BOOKED'::character varying, 'COMPLETED'::character varying, 'CANCELLED'::character varying])::text[])))
);

CREATE TABLE public.medical_records (
    record_id character varying(255) NOT NULL,
    diagnosis character varying(500) NOT NULL,
    treatment character varying(1000) NOT NULL,
    treatment_cost numeric(12,2) NOT NULL,
    appointment_id character varying(255) NOT NULL
);

CREATE TABLE public.insurance_policies (
    policy_id character varying(255) NOT NULL,
    coverage_amount numeric(12,2) NOT NULL,
    policy_type character varying(100) NOT NULL,
    provider_name character varying(100) NOT NULL,
    patient_id character varying(255) NOT NULL
);

CREATE TABLE public.insurance_claims (
    claim_id character varying(255) NOT NULL,
    claim_amount numeric(12,2) NOT NULL,
    status character varying(20) NOT NULL,
    medical_record_id character varying(255) NOT NULL,
    CONSTRAINT insurance_claims_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'APPROVED'::character varying, 'REJECTED'::character varying])::text[])))
);

-- Primary keys and existing one-to-one unique constraints.

ALTER TABLE ONLY public.appointments
    ADD CONSTRAINT appointments_pkey PRIMARY KEY (appointment_id);

ALTER TABLE ONLY public.doctors
    ADD CONSTRAINT doctors_pkey PRIMARY KEY (doctor_id);

ALTER TABLE ONLY public.insurance_claims
    ADD CONSTRAINT insurance_claims_pkey PRIMARY KEY (claim_id);

ALTER TABLE ONLY public.insurance_policies
    ADD CONSTRAINT insurance_policies_pkey PRIMARY KEY (policy_id);

ALTER TABLE ONLY public.medical_records
    ADD CONSTRAINT medical_records_pkey PRIMARY KEY (record_id);

ALTER TABLE ONLY public.patients
    ADD CONSTRAINT patients_pkey PRIMARY KEY (patient_id);

ALTER TABLE ONLY public.medical_records
    ADD CONSTRAINT uk2nyonrbplqq716buy7u4ghmt8 UNIQUE (appointment_id);

ALTER TABLE ONLY public.insurance_policies
    ADD CONSTRAINT uk3kyiq9khr2vm432h4agg5c70b UNIQUE (patient_id);

ALTER TABLE ONLY public.insurance_claims
    ADD CONSTRAINT ukepqa7r00ctn4q76t9gix5qnn7 UNIQUE (medical_record_id);

-- Foreign keys retain the existing names and referential actions.

ALTER TABLE ONLY public.appointments
    ADD CONSTRAINT fk_appointment_doctor FOREIGN KEY (doctor_id) REFERENCES public.doctors(doctor_id);

ALTER TABLE ONLY public.appointments
    ADD CONSTRAINT fk_appointment_patient FOREIGN KEY (patient_id) REFERENCES public.patients(patient_id);

ALTER TABLE ONLY public.insurance_claims
    ADD CONSTRAINT fk_insurance_claim_medical_record FOREIGN KEY (medical_record_id) REFERENCES public.medical_records(record_id);

ALTER TABLE ONLY public.insurance_policies
    ADD CONSTRAINT fk_insurance_policy_patient FOREIGN KEY (patient_id) REFERENCES public.patients(patient_id);

ALTER TABLE ONLY public.medical_records
    ADD CONSTRAINT fk_medical_record_appointment FOREIGN KEY (appointment_id) REFERENCES public.appointments(appointment_id);
