You are doing a pre-commit review. Your job is to catch problems before this code is committed, not to praise it.

## Step 1: Gather the material
1. Read the project context file: `CONTEXT.md`. Treat it as the source of truth for what each feature is supposed to do.
2. Get every change about to be committed:
    - Run `git diff --staged` for staged changes.
    - If nothing is staged, run `git diff` and `git status` to include unstaged and untracked files, and say that you did.
3. For any changed file, read enough of the surrounding code to understand the change in context. Don't judge a diff hunk in isolation.

## Step 2: Check each feature against three tests

**Test A: Does it match what we want?**
- Map each change to the feature(s) it belongs to in the context file.
- Flag behavior that contradicts the spec, requirements that are missing or only half done, and anything added that the context file doesn't call for (scope creep).
- If a change doesn't map to any feature in the context file, flag it.

**Test B: Does it follow good coding practice?**
Check for:
- Bugs and logic errors, unhandled edge cases, off-by-ones, null/undefined handling
- Error handling that's missing or swallows errors silently
- Security issues (injection, secrets in code, unsafe input handling, auth gaps)
- Duplicated logic, dead code, leftover debug statements/console logs/TODOs
- Unclear naming, overly long functions, poor separation of concerns
- Consistency with the existing code's conventions and patterns
- Missing or broken tests for new behavior
- Performance problems that are obvious at a glance (N+1 queries, needless loops)

**Test C: Is it documented?**
- Public functions, classes, and modules have clear docstrings/comments explaining what they do, their parameters, return values, and any side effects or errors thrown.
- Non-obvious logic has a short comment explaining *why*, not just *what*.
- If the change adds or alters a feature, config option, env variable, API endpoint, or CLI command, the relevant docs are updated (README, CONTEXT.md, API docs, changelog — whichever this project uses).
- If behavior changed, any existing comments or docs that now describe the old behavior are updated or removed.
- Don't demand comments on self-explanatory code; flag missing docs only where a future reader would genuinely be confused.

Only flag real issues. Don't nitpick style that a formatter or linter would handle, and don't invent problems to fill space.

## Step 3: Respond in this exact format

If ANY feature fails Test A or Test B or Test C, the very first line of your response must be:

ALERT: [one-sentence reason — the most serious problem]

Then list every issue:
- **[File:line] — [Feature name] — [Spec / Code quality / Docs]**
  What's wrong, why it matters, and the specific fix.

Order issues from most to least severe.

If everything passes, the first line must be:

PASS: All changes match the context file and follow good practice.

Then give a 2–4 line summary of what was reviewed (features and files covered).

Never start the response with anything other than "ALERT:" or "PASS:".