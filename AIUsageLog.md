# Milestone 1 AI Usage Log

Below are the individual ai usage logs seperated for each member and milestone.

## Milestone 1b

### Jaden's Ai Usage for 1b below

**Status:** Accepted

**Context:**
The project involves creating a student marketplace for John Abbott College where students can chat, browse, buy, sell, and request items. The immediate goals were to draft a concise project description and configure the initial GitHub repository according to specific assignment requirements regarding branch protection and commit quality.

**Decision (AI Prompts & Outputs):**

- **Prompt:** Requested refinement of a draft description for the student marketplace app.
  - **Output:** Provided three distinct, concise options (Direct, Warm, and Elevator Pitch) clarifying _what_ the application is and _its purpose_, along with tips on effective project descriptions.
- **Prompt:** Provided an image detailing repository setup requirements ("image_1fb76d.png") and requested help.
  - **Output:** Broke down the image into actionable steps: configuring branch protection rules on `main` (requiring deployments, status checks, PR reviews, and linear history), adopting the Conventional Commits format, and outlining the grading criteria.
- **Prompt:** Asked for clarification on where to configure the deployment environments for the branch rules (specifically asking if it is done in CI/CD).
  - **Output:** Confirmed that deployment environments and status checks will eventually be configured in the CI/CD pipeline (like GitHub Actions), but clarified that the current step only requires checking the requirement boxes in the repository settings.

**Rationale [MANUAL INPUT REQUIRED]:**
Here I wanted to ensure our app desciption was well worded and organized, so I gave it a rundown of the project and my draft of the descirption. Then the branch changes I had not done before so I wanted to ensure I did them correctly so I used ai to guide me.

**Consequences / AI Generation Limits:**

- **AI-Generated Code:** 0% (No code was generated during this session)
- **Impact:** AI assistance helped streamline the drafting of the project description and clarified complex repository configuration instructions, ensuring the team starts with best practices and meets assignment criteria.
- **Chat Transcript:** [[Chat link]](https://share.gemini.google/K7V3WTVWLVRA)

### Luca's Ai Usage below

https://chatgpt.com/s/cx_6abfce2610188191a265bcdbcb996900

### Chris's Ai Usage below

## Milestone 1c

### Jaden's Ai Usage below

**Status:** Accepted

**Context:**
The project involves creating a student marketplace tailored for John Abbott College (JAC) students to chat, browse, search, buy, sell, and request items. The immediate goals were to define and discover specific user needs, evaluate the competitive landscape against existing alternatives, establish safety and verification mechanisms, and synthesize brainstorming into structured product requirements.

**Decision (AI Prompts & Outputs):**

- **Prompt:** Requested help defining user needs, evaluating competitors, and selecting a viable product concept based on an initial pitch of the JAC student marketplace.
  - **Output:** Formulated primary user personas and pain points, evaluated competitors (Facebook Marketplace, Omnivox, Kijiji), proposed "JAC-Exchange" as a core product concept with institutional email gating, and defined a initial MVP scope.
- **Prompt:** Validated competitive analysis assumptions regarding Facebook Marketplace and Omnivox, and proposed email verification and warning banners for trust.
  - **Output:** Validated hypotheses while pointing out unofficial student Facebook Groups as real competitors and addressing cold-start/seasonality risks. Recommended trust and safety features including peer ratings, profile badges, preset JAC campus safe-zone selectors, and in-chat safety banners.
- **Prompt:** Asked for further exploration of student needs and pain points.
  - **Output:** Detailed academic friction points (course codes, textbook editions, lab gear), schedule constraints, ghosting dynamics, and mapped features to physical JAC campus locations (Herzberg, Stewart Hall, Casgrain, AME).
- **Prompt:** Requested a summary recap of all key topics brainstormed up to that point.
  - **Output:** Provided a structured executive summary spanning Product Concept & Positioning, User Needs & Pain Points, Trust & Safety Architecture, and Core MVP Scope.
- **Prompt:** Asked for a quick overview focused specifically on comprehensive user needs rather than just pain points.
  - **Output:** Categorized core user needs into three operational buckets: Discovery & Match Accuracy, Convenience & Speed, and Trust & Social Friction.

**Rationale [MANUAL INPUT REQUIRED]:**
Here the rational was mostly to try and get it to help us research or think of other perpectives or other things we missed. For example, competitors, user features to help reduce pain (such as scams) and possible issues and more.

**Consequences / AI Generation Limits:**

- **AI-Generated Code:** 0% (No codebase implementation was performed during this session; the focus was entirely on product strategy, user research, and requirements discovery)
- **Impact:** AI assistance accelerated the product discovery phase by highlighting domain-specific campus considerations (such as JAC course code filtering, semester seasonality, and preset campus safe-zone meetups) and converting open brainstorming into actionable product specifications.
- **Chat Transcript:** https://share.gemini.google/dQmVKGT7GTuZ (this is the same chat as the pdf provided in /ProjectResearch)!!

### Luca's Ai Usage below

```
# Write JACpot project vision

> Continuing from [Write Final Project Document](chatgpt-conversation://6abfbc06-7e10-83ea-9946-805b7458f6c9): Create a polished final document for the JACpot Student Marketplace project-vision milestone using the user's assignment requirements and notes from this conversation. Preserve the team's actual process rather than inventing elaborate brainstorming that did not happen. Include: brainstorming/raw-session summary, AI discussion log/summary, targeted research findings (user needs, pain points, competitors/gaps, domain/technical constraints), documented team decision-making and final selection, final project vision, and a compelling ~60-second elevator pitch. Make it sound like authentic college-student work: clear and professional but not overly corporate. JACpot is a John Abbott College student marketplace, similar to Facebook Marketplace but campus-specific. Features: listings, search/filtering, requests/message board, student profiles, chat, ratings, JAC email verification, textbook-specific info including used access-code tags. Safety ideas: full names/student IDs, ratings, suggested meetup spots such as Hertzberg Library/Agora/Zen Zone, in-app warnings, no in-app purchasing and payment in person. Competitors: John Abbott Facebook groups and Facebook Marketplace. The team was aligned on JACpot from the beginning; don't fabricate rejected concepts. Explicitly address the rubric's target user/client, core problem, app category, and tech-driven unique solution.

<details><summary>10 previous messages</summary>

> I’ll review the rest of the referenced conversation, then put together a final document that reflects the team’s actual discussion and covers each rubric item.
>
> <details><summary>Explored a search</summary>
>
> <details><summary>Explored a file, a search</summary>
>
> - Read `~/.codex/plugins/cache/openai-primary-runtime/documents/26.904.11930/skills/documents/SKILL.md`
> - Searched for `JACpot|Write Final Project|student marketplace` in `MEMORY.md`
>
> </details>
>
> Tool call
>
> Tool: read_thread
> Status: completed
>
> </details>
>
> The referenced chat confirms that JACpot was the team’s starting point, and its saved message cuts off part of the notes. I’ll use the details you supplied, keep research claims framed as findings to validate, and avoid presenting a reconstructed summary as verbatim session logs. I’m using the document workflow so you get an editable file.
>
> <details><summary>Created a file, ran 3 commands</summary>
>
> - Ran `/Users/lucamaiolo/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/bin/node /Users/lucamaiolo/.codex/plugins/cache/openai-primary-runtime/documents/26.904.11930/skills/documents/container_tools/mark_artifact_operation_started.mjs --operation-kind create --expected-output-count 1 --output-format docx`
> - Wrote `./work/build_jacpot.py` (+99 -0)
> - Ran `mkdir -p outputs work/rendered && /Users/lucamaiolo/.cache/codex-runtimes/codex-primary-runtime/dependencies/python/bin/python3 work/build_jacpot.py && /Users/lucamaiolo/.cache/codex-runtimes/codex-primary-runtime/dependencies/python/bin/python3 /Users/lucamaiolo/.codex/plugins/cache/openai-primary-runtime/documents/26.904.11930/skills/documents/render_docx.py outputs/JACpot_Project_Vision.docx --output_dir work/rendered`
> - Ran `ls -lh work/rendered/page-*.png`
>
> </details>
>
> Viewed 6 images
>
> The first render has a table splitting across pages and a short third page. I’m tightening the layout and keeping the comparison together so the document reads cleanly.
>
> it should be a markdwon file
>
> <details><summary>Edited a file</summary>
>
> - Wrote `./work/build_jacpot.py` (+20 -9)
>
> </details>
>
> Got it—Markdown is the right format. I’ll deliver the final document as a `.md` file and remove the Word draft.
>
> <details><summary>Created a file, ran a command</summary>
>
> - Wrote `./outputs/JACpot_Project_Vision.md` (+76 -0)
> - Ran `rm -f outputs/JACpot_Project_Vision.docx && wc -w outputs/JACpot_Project_Vision.md && rg -n '^#|^\*\*Decision record|^\*Approximate' outputs/JACpot_Project_Vision.md`
>
> </details>
</details>

Created the [JACpot project vision document](./outputs/JACpot_Project_Vision.md) as a Markdown file. It covers the milestone requirements and keeps the team’s process honest: JACpot was the shared idea from the start, and the available notes are identified as a summary rather than a verbatim log.

```

### Chris's Ai Usage below
