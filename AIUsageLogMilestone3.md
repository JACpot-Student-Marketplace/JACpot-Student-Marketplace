## Milestone 3 AI Usage

### Jaden's Ai Usage for Milestone 2 below

**Status:** Accepted

**Context:**
The project involves developing **JACpot**, a peer-to-peer student marketplace for John Abbott College built with Kotlin Multiplatform and Jetpack Compose. Across Milestone 2, the goals were to transition from initial planning into concrete UI and client-side state implementation: defining milestone coordination and risk analysis commitments, setting up an AI-assisted Research-Plan-Implement-Validate (R-PIV) workflow, implementing reactive listing state management (`ListingsViewModel` and `LocalListings`), building the Home screen, refactoring the adaptive Browse feed and `ListingDetailsDialog` (including multi-photo gallery support and **Kamel** image loading), implementing the validated **Create Listing** flow (`CreateScreen.kt` and `ListingForm.kt`), writing unit tests, and documenting changes via Pull Requests and ADRs.

**Decision (AI Prompts & Outputs):**

- **Prompt (Gemini — Milestone Planning & R-PIV Workflow):** Requested assistance structuring Milestone 2 coordination commitments, risk analysis inputs, and the team's AI-assisted R-PIV (Research-Plan-Implement-Validate) development workflow.
    - **Output:** Helped formalize milestone deliverables, risk mitigation notes, and structured prompts/checklists so AI-assisted changes remained aligned with project architecture and team conventions.
- **Prompt (Gemini — State Management, Browse Feed, & `ListingDetailsDialog`):** Asked for guidance and code structures to hoist listing state in `App.kt`, refactor the Marketplace browse feed for mobile/desktop layouts, and implement `ListingDetailsDialog`.
    - **Output:** Outlined root-level state hoisting using `rememberSaveable` with a custom `listSaver` and `LocalListings` composition locals, an adaptive `BoxWithConstraints` grid layout for the browse feed, and a Material 3 `ListingDetailsDialog` supporting seller details and owner actions (`Edit`, `Mark as Sold`, and two-step `Delete` confirmation).
- **Prompt (Gemini — `ListingsViewModel` Unit Tests & Git Merge Conflict Resolution):** Requested unit test coverage for `ListingsViewModel` and help resolving Git merge conflicts in shared UI components (such as `JACpotTextField`).
    - **Output:** Generated unit test cases verifying active listing filtering, ownership validation, item creation, and status updates using deterministic mock timestamps, and showed how to merge conflicting component parameters cleanly without breaking existing screens.
- **Prompt (Gemini — Initial `ListingForm.kt` & `CreateSellItemForm` Draft):** Shared `RequestForm.kt`, `CreateRequestScreen.kt`, and the prototype `CreateSellItemForm` to generate a matching `ListingForm.kt` and updated create screen.
    - **Output:** Produced an initial `ListingForm.kt` (`parsePriceCents`, `ListingFormErrors`, `validateListingForm`, `buildListing`) and updated form structure, though it lacked visibility into cross-file models and database rules (`LocalListings`, `ItemCondition`, conditional textbook fields, and photo URL handling).
- **Prompt (Claude Desktop / Claude Code — Full `CreateScreen.kt` & `ListingForm.kt`):** Opened the local repository in Claude Code, instructed it to read the codebase for full context while operating strictly read-only (outputting complete code without modifying project files directly), and noted project constraints (Supabase Storage not yet connected, working off a nested branch). Requested the complete `CreateScreen.kt` and `ListingForm.kt`.
    - **Output:** Generated the complete `ui/screens/CreateScreen.kt` and `ListingForm.kt` wired to `LocalListings.current.addListing(...)`, adding `ItemCondition`, optional `courseCode`, conditional textbook fields (`author`, `releaseDate`, `accessCodeUsed` matching the `listings_book_details` database rule), and line-delimited `photoUrls` validation (up to 5 links).
- **Prompt (Claude Desktop / Claude Code — `PhotoGallery` in `ListingDetailsDialog`):** Requested a multi-photo gallery composable for `ListingDetailsDialog` to display multiple listing photos alongside Kamel image loading.
    - **Output:** Generated `PhotoGallery(listing: Listing)` featuring a primary `4:3` `ListingPhoto` preview, a bottom-right photo counter badge (`selected + 1 / photos.size`), a horizontally scrollable row of `64.dp` clickable thumbnails with an active selection border, and `rememberSaveable(listing.id)` index persistence.
- **Prompt (Gemini — Pull Request Descriptions & ADR Documentation):** Provided implementation details and generated code across Milestone 2 features to draft Pull Request descriptions (`Description` and `Resolution Description`) and compile this AI Usage Log ADR.
    - **Output:** Generated standardized PR descriptions summarizing UI, state, validation, Kamel image loading, and unit test updates, along with this Milestone 2 ADR entry.

**Rationale [MANUAL INPUT REQUIRED]:**
For this milestone, I used tried to save my ai usage for the end, and used it mostly to guide me as I had existing code I could base most of my work off at some points. When I did use ai, it was on the parts I didn't want to do, had to do in a shorter time period, or just overall was already planning on making with ai when I thought about what I would or would not use ai for to keep it below the 50%. I always use gemini, but what happened here is that these files got too complex or I did not update gemini enough (I thought it could see my repo)
so I ended up using Claude code (which I recently purchased the other week) to make some of the final files for me (stated above).

**Consequences / AI Generation Limits:**

- **AI-Generated Code:** ~45% (AI assisted with scaffolding `ListingForm.kt`, `CreateScreen.kt`, `PhotoGallery`, initial `ListingsViewModel` unit tests, and state/layout patterns; all Kamel integration, repository wiring, branch management, merge conflict resolution, manual code integration, compilation fixes, and runtime testing were performed manually to stay within the 50% AI generation limit)
- **Impact:** Combining Gemini (for architectural planning, state hoisting patterns, unit tests, PR descriptions, and ADR formatting) with Claude Code (for read-only inspection of local models, ViewModels, and database rules) accelerated UI and validation development while preserving code consistency with existing screens like `CreateRequestScreen.kt`.
- **Chat Transcript:** [[Gemini Chat link]]([https://share.gemini.google/na0GEaY3Qyzx]) *(Claude Desktop / Claude Code sessions were run locally)*


### Luca's AI Usage for the Request Details task

**Status:** Accepted

**Context:**
JACpot is a peer-to-peer student marketplace for John Abbott College, built with Kotlin Multiplatform and Compose. The task ("Request Details and AI Chat Bar Start") asked for a request detail view, then an AI chat bar for creating requests. This session covered the request details part only: showing the full information of a request from the Requests feed. The AI chat bar, draft area and listing matches are not done yet.

**Decision (AI Prompts & Outputs):**

- **Prompt (Claude Code, task statement):** Pasted the task description and acceptance criteria and asked to build the request detail screen.
  - **Output:** Explored the repo, then built a full-screen detail route (`RequestDetailScreen`, a nav key, a router entry), a `formatDate` helper, `RequestStatus.label` and a date test. Tests passed. This version was later thrown away.
- **Prompt (Claude Code, "make the pr" / "push"):** Asked for a PR, then to push.
  - **Output:** Created a branch, committed and pushed it. The PR creation was rejected by me before it ran. I then asked whether it had been pushed, and the branch was deleted from GitHub at my request.
- **Prompt (Claude Code, "check how my teammate did the details on normal listings, on main"):**
  - **Output:** Read the teammate's `ListingDetailsDialog` on `main` (an `AlertDialog` opened from the screen with a `rememberSaveable` id, no nav route) and recommended matching it. A follow-up question ("whats the dif") produced a dialog-vs-screen comparison.
- **Prompt (Claude Code, "delete the page branch, make the dif version"):**
  - **Output:** Rebuilt the details as `RequestDetailsDialog`, styled like the listing dialog (status label, title, "Requested by X · posted Nh ago", pills, budget, description, Close). Made `RequestCard` clickable and kept the open request id in `RequestsScreen`. Tests passed.
- **Prompt (Claude Code, "lint it"):**
  - **Output:** Ran ktlint, which found wrapping and line-length issues in the new file, then ran `ktlintFormat`. Lint and tests passed afterwards.
- **Prompt (Claude Code, "MERGE CONFLICTS", "just push as a new commit"):**
  - **Output:** Found that the feed PR had been merged into `main`, rebased the two commits onto `main` without conflicts, then pushed the result as a merge commit on top of the remote branch so no force-push was needed.

**Rationale [MANUAL INPUT REQUIRED]:**
I used AI here, as it was essentially a repeated logic of what Jaden had done. Also I was just lazy on this part and I didn't feel like doing this specific part of the milestone. 
**Consequences / AI Generation Limits:**

- **AI-Generated Code:** _[MANUAL INPUT REQUIRED]_ For this task, Claude Code wrote essentially all of the code in the PR (`RequestDetailsDialog.kt`, the `RequestCard` and `RequestsScreen` changes, `formatDate`, `RequestStatus.label`, `FormatDateTest`). The design choice to copy the teammate's dialog pattern came from reading their code on `main`. Report the share honestly against the team's 50% limit, and count your own review or edits only if you made them.
- **Not verified by AI:** The dialog was not run or looked at in the app. Only `:shared:jvmTest` and `:shared:ktlintCheck` were run.
- **Mistakes during the session:** The AI first claimed a branch was local only when it had already been pushed, and once ran a stray `git stash` that did nothing. Neither changed the final code.
- **Impact:** AI sped up the exploration, the dialog and the PR text, and kept the new code consistent with the existing listing dialog. The AI chat parts of the task are still to do.
- **Chat Transcript:** Claude Code sessions run locally, so there is no share link. 
