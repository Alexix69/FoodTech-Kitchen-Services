# DB Migration Notes — RBAC Role Field (BE-1)

**Feature**: 001-rbac-role-access  
**Date**: 2026-04-02  
**Phase**: BE-1

## Column Addition: `users.role`

The `User` domain model now carries a `UserRole role` field (`MESERO | COCINERO | BARTENDER`).  
The corresponding JPA entity column annotation is added in Phase BE-4b.

### Development (ddl-auto: create / create-drop)

No action required. Spring Boot regenerates the schema on startup and the column
is automatically created by Hibernate from `@Column(name = "role", nullable = true)`
on `UserEntity.role`.

### Staging / Production (ddl-auto: none or validate)

Run the following migration **before** deploying the RBAC build:

```sql
ALTER TABLE users ADD COLUMN IF NOT EXISTS role VARCHAR(20);
```

- `nullable = true` is intentional: pre-existing users have no role at first.
  They are handled at login time via the `ROLE_NOT_ASSIGNED` recovery flow
  (`PATCH /api/users/{id}/role`).
- No backfill migration is required; the recovery flow covers all legacy users
  on next login.

### Verification

After startup on a fresh schema:
```sql
\d users
-- role column should appear: role character varying(20)
```

After startup on an existing dev database:
```sql
SELECT id, username, role FROM users LIMIT 5;
-- role column should exist; values may be NULL for pre-existing rows
```

### Rollback

```sql
ALTER TABLE users DROP COLUMN IF EXISTS role;
```
