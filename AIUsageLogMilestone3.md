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
I did this with Ai for 2 reasons:
- It was essentially a rehash of Jaden's logic for the listings tab
- I was lazy and didn't want to do this specific part
**Consequences / AI Generation Limits:**

- **AI-Generated Code:** _[MANUAL INPUT REQUIRED]_ For this task, Claude Code wrote essentially all of the code in the PR (`RequestDetailsDialog.kt`, the `RequestCard` and `RequestsScreen` changes, `formatDate`, `RequestStatus.label`, `FormatDateTest`). The design choice to copy the teammate's dialog pattern came from reading their code on `main`. Report the share honestly against the team's 50% limit, and count your own review or edits only if you made them.
- **Not verified by AI:** The dialog was not run or looked at in the app. Only `:shared:jvmTest` and `:shared:ktlintCheck` were run.
- **Mistakes during the session:** The AI first claimed a branch was local only when it had already been pushed, and once ran a stray `git stash` that did nothing. Neither changed the final code.
- **Impact:** AI sped up the exploration, the dialog and the PR text, and kept the new code consistent with the existing listing dialog. The AI chat parts of the task are still to do.
- **Chat Transcript:** Claude Code sessions run locally, so there is no share link. The reconstructed log is in `CONVERSATION_LOG.md`.

### Chris' AI Usage for milestone 3

**Title:** AI Assistance for Shared Models, the Ktor/Supabase Server, and Auth Tests

**Status:** Accepted

**Context:**
For JACPot, the client and server needed the same records for users, listings, requests, conversations, messages, and buy requests, and the Ktor server had to read and write the Supabase tables that were already created. The login screen, create-account screen, and their validation were already written. This session was used to shape those shared models, stand up the server and shared Ktor client, and add tests around the auth validation that was already in place.

**Decision (AI Prompts & Outputs):**
I used Cursor as an architectural advisor and to generate the shared models, the server module, and the auth tests.

- **Prompt 1:** Pasted the JACPot proposal and asked the assistant to stand by.
  - *Output:* Restated the three pillars, the shared-model plan, and the Ktor, Compose, and database split.
- **Prompt 2:** Asked how to start the database work for task F5, including the data classes needed to store the Part A records.
  - *Output:* Proposed shared `@Serializable` models with `String` ids and prices in cents, plus server-only document types, indexes, and the Part A validation rules.
- **Prompt 3:** Asked whether that design matched task F3 (shared classes and validation for User, Listing, Request, Conversation, Message, and BuyRequest).
  - *Output:* Confirmed that it did, and listed what was still missing: the files were not in `shared` yet, `ObjectId` had to stay out of `commonMain`, and the validation functions were not written yet.
- **Prompt 4:** Asked whether switching from MongoDB to Supabase would change those models.
  - *Output:* The shared classes and validation stayed the same. Supabase changed the storage to UUID tables, and the Ktor server remained the only component allowed to hold the Supabase secret.
- **Prompt 5:** Asked for the Supabase tables.
  - *Output:* Provided the SQL for `users`, `listings`, `listing_photos`, `requests`, `conversations`, `messages`, and `buy_requests`, including the email, price, photo, expiry, and one-conversation-per-pair constraints.
- **Prompt 6:** Asked how to run `select *` in the Supabase SQL editor, then reported that the query succeeded with no rows.
  - *Output:* Showed `select * from users` and a sample insert for one `@johnabbottcollege.net` account.
- **Prompt 7:** Asked what to add for the shared models, and where.
  - *Output:* Specified `shared/src/commonMain/kotlin/com/example/multiplatformapplication/model/` and supplied `Enums.kt`, `User.kt`, `Listing.kt`, `Request.kt`, `Conversation.kt`, `Message.kt`, `BuyRequest.kt`, and `Validation.kt`.
- **Prompt 8:** Asked what `THIRTY_DAYS_MS` is, and whether the proposal states the 30-day rule.
  - *Output:* Explained it as 30 days in milliseconds for `requestExpiresAt`, and pointed to the proposal’s 30-day request lifetime. The constant name itself is not in the proposal.
- **Prompt 9:** Asked how to set up the Ktor server, keep the Supabase URL and secret on the server only, and add one shared Ktor client for every platform.
  - *Output:* Laid out the `:server` module, `server/.env`, `SupabaseGateway`, `Application.kt` on port 8080, and `JacpotApi` with a base URL and no Supabase credentials.
- **Prompt 10:** Asked where the `server` folder goes, what to add to `gradle/libs.versions.toml`, and for the code with full paths. Also asked whether Android Studio could generate the module.
  - *Output:* Put `:server` next to `shared`, listed the Ktor 3.6.1 catalog entries, and gave the file contents. A separate Ktor project template was the wrong starting point.
- **Prompt 11:** Asked the assistant to add `DotEnv.kt`, `SupabaseGateway.kt`, `Application.kt`, and the platform API clients.
  - *Output:* Wrote those server files, added `NewListing` to the shared listing model, and added `ApiPlatform` implementations for desktop, Android (`10.0.2.2`), JavaScript, and Wasm. User reads omit `password_hash`.
- **Prompt 12:** Asked how to run ktlint, whether `96% EXECUTING` on `:server:run` was normal, and why `ktlintCheck` still failed after `ktlintFormat`.
  - *Output:* Gave `ktlintFormat` and `ktlintCheck`. The server staying at 96% means it is running. The remaining failure was the filename rule, so `DotEnv.kt` had to be renamed to `ServerEnv.kt`.
- **Prompt 13:** Asked for tests for a valid account, required fields, a non-Abbott email, and a login that requires a valid email and password.
  - *Output:* Supplied `AuthFormTest.kt`, which calls `validateCreateAccount`, `validateLogin`, and `authenticate`. The login screen, create-account screen, and that validation were already written and were not generated here.
- **Prompt 14:** Pasted the `:shared:allTests` failure.
  - *Output:* The failure was a missing Android SDK on `:shared:testAndroidHostTest`. The auth tests themselves run with `:shared:jvmTest`.

**Rationale:**

Ai was super crucial to my part of this milestone as it was unknown territory doing all the supabase integration with the Ktor server module. All my ai usage except for tests for my login and create screens revolved around the backend and the communication with supabase, I wouldn't have been able to complete all of that for this milestone had I not worked with ai and understand from what it gave me. As per what was said in class, it was heavily incentivised for this use and I feel that I truly worked well with it.

**Consequences / AI Generation Limits:**

- **Total AI generation counted toward the limit:** ~45%.
- I stayed under the 50% limit. The login screen, create-account screen, and their validation were written by me (`LoginScreen.kt`, `CreateAccountScreen.kt`, `AuthForm.kt`, and the demo login check). In this session the AI generated the shared models (`User`, `Listing`, `Request`, `Conversation`, `Message`, `BuyRequest`, the enums, and `Validation.kt`) and `AuthFormTest.kt`. That is about 310 AI-generated lines against about 370 lines I wrote for those screens and their validation.
- **The AI-generation limit does not apply to the backend Ktor and Supabase integration.** The server module, the Supabase SQL, the gateway, and the shared Ktor client that calls the server are excluded from the percentage above. That backend work in this session was AI-generated, and it is reported separately because the limit does not cover it.
- The AI work gave the app one set of shared records, a server that can read and write Supabase without putting the secret in the client, and tests that check the account and login rules I had already implemented.