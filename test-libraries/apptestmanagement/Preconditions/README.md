# Preconditions

This folder contains `.preconditions.md` files. These describe the **state** the environment must be in before a test can start.

## Rules

1. **State, Not Action**: Use statements of fact (e.g., "User is logged in"), not steps (e.g., "User clicks login").
2. **Naming**: Use `<Feature>.preconditions.md`.
3. **Tags**: Each block must have a stable tag: `@Preconditions_<Feature>_<Name>`.
4. **Tips**: Use the `💡Tip:` section to document:
    * Faker API endpoints and payloads.
    * Database queries or flags.
    * Local settings overrides.

## Example
```markdown
@Preconditions_Cellwarn_GroupD
- The subscriber's reported grid_id is in an affected area.
- The report is less than 30 minutes old.
💡Tip: Use POST /faker/subscribers/{id} with `{ "gridId": "..." }`.
```
