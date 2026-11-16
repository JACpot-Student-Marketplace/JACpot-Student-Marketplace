-- Apply in the Supabase SQL editor before registration can succeed.
-- If the unique index fails, remove duplicate users.email rows and run it again.
-- sessions.user_id uses the same type as users.id. users.id must be a primary key.
-- The server sends a uuid string for new user ids.

alter table public.users add column if not exists password_hash text;

create unique index if not exists users_email_key on public.users (email);

do $$
declare
    id_type text;
begin
    select format_type(a.atttypid, a.atttypmod)
    into id_type
    from pg_attribute a
    join pg_class c on c.oid = a.attrelid
    join pg_namespace n on n.oid = c.relnamespace
    where n.nspname = 'public'
      and c.relname = 'users'
      and a.attname = 'id'
      and not a.attisdropped;

    if id_type is null then
        raise exception 'public.users.id was not found';
    end if;

    execute format(
        'create table if not exists public.sessions (
            token_hash text primary key,
            user_id %s not null references public.users (id) on delete cascade,
            expires_at timestamptz not null,
            created_at timestamptz not null default now()
        )',
        id_type
    );
end $$;

alter table public.sessions enable row level security;

grant select, insert, update, delete on table public.sessions to service_role;

revoke select (password_hash) on table public.users from anon, authenticated;
