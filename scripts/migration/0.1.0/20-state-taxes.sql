--WARNING: This script file updates specific data.

update address a set state_id = 9 where a.state_id = 32;
UPDATE public.state
SET book_taxes = 9.000::numeric(6, 3)
WHERE id = 26::bigint;

UPDATE public.state
SET book_taxes = 7.000::numeric(6, 3)
WHERE id = 39::bigint;

UPDATE public.state
SET book_taxes = 0.000::numeric(6, 3)
WHERE id = 38::bigint;

UPDATE public.state
SET book_taxes = 15.000::numeric(6, 3)
WHERE id = 34::bigint;

UPDATE public.state
SET book_taxes = 4.000::numeric(6, 3)
WHERE id = 19::bigint;

UPDATE public.state
SET book_taxes = 6.000::numeric(6, 3)
WHERE id = 10::bigint;

UPDATE public.state
SET book_taxes = 10.000::numeric(6, 3)
WHERE id = 9::bigint;

UPDATE public.state
SET book_taxes = 10.000::numeric(6, 3)
WHERE id = 15::bigint;

UPDATE public.state
SET book_taxes = 10.000::numeric(6, 3)
WHERE id = 7::bigint;

UPDATE public.state
SET book_taxes = 7.000::numeric(6, 3)
WHERE id = 23::bigint;

UPDATE public.state
SET book_taxes = 9.500::numeric(6, 3)
WHERE id = 33::bigint;


alter table public.order_score
    drop constraint fk6a86q5a29titsgqe9na9n3ksl;

alter table public.order_score
    add constraint fk6a86q5a29titsgqe9na9n3ksl
        foreign key (order_id) references public.product_order
            on delete cascade;

alter table public.product_order
    drop constraint fkaliq7cgj7d8yog1ogga5k6d20;

alter table public.product_order
    add constraint fkaliq7cgj7d8yog1ogga5k6d20
        foreign key (identity_id) references public.identity
            on delete cascade;

alter table public.product_order
    drop constraint fksv7d6hvsn70hwiwomuxpqm9c5;

alter table public.product_order
    add constraint fksv7d6hvsn70hwiwomuxpqm9c5
        foreign key (delivery_address_id) references public.address
            on delete cascade;
alter table public.identity
    drop constraint fkmfmx4an4hmfjh4cn0ub9i8bkp;

alter table public.identity
    add constraint fkmfmx4an4hmfjh4cn0ub9i8bkp
        foreign key (address_id) references public.address
            on delete cascade;

alter table public.address
    drop constraint fk4cx5jughttw4h3qfxrcitbtxl;

alter table public.address
    add constraint fk4cx5jughttw4h3qfxrcitbtxl
        foreign key (state_id) references public.state
            on delete cascade;

delete from state s where s.book_taxes is null;