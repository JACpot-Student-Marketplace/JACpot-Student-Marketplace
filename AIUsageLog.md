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

ADR: AI Assistance for Marketplace Navigation and CI Formatting
Status: Accepted
Context
The Login, Marketplace, and Create screens existed separately. I needed to connect them with Navigation3 and resolve a Kotlin formatting failure reported by CI.
Decision (AI Prompts & Outputs)
- I supplied a Navigator and Router pattern and asked the AI to link the screens. It adapted the pattern to the project, made App() open Router(), and connected Login → Marketplace → Create.
- The AI connected item creation to the Marketplace listing view and added the required navigation and serialization dependencies.
- I pasted the CI ktlintCommonMainSourceSetCheck failure. The AI formatted the affected files and corrected the remaining comments and indentation.
Rationale:
I used AI for this section because it would have been very tedious to do by myself. I understand all the code as it is all either given in class or was assignment work. 

The shared Kotlin lint check and JVM compilation passed after the changes. The compiler still reports a non-failing deprecation warning for menuAnchor(). Review the navigation and listing flow yourself before submitting, and add your own contributions and verification details here.
Chat link: https://chatgpt.com/s/cx_6abfce2610188191a265bcdbcb996900

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

AI usage log — Architecture Decision Record

### Title

AI assistance for organizing the JACpot project vision and elevator pitch

### Status

Accepted

### Context

We had a shared JACpot concept and a set of team notes, but the milestone required several separate deliverables: a record of brainstorming, research findings, the team's decision process, a clear project vision, and a one-minute pitch. We used AI to help organize that material into a readable document. Our notes were incomplete as a transcript, so accurate wording about what the team actually did mattered.

### Decision — AI prompts and outputs

- **Initial prompt:** We supplied the assignment requirements and our notes describing JACpot as a student marketplace that would replace scattered platforms. **Visible AI output:** The AI said it would build a polished document and keep the account authentic to the team's process, including that JACpot was the concept from the start. The referenced conversation does not contain a completed document from that exchange.
- **Document request:** We asked AI to create the final project-vision document with the specified features, competitors, constraints, decision record, and a roughly 60-second pitch. We explicitly said not to invent rejected concepts. **Output:** AI drafted the Markdown document, including the session summary, working research findings, final vision, and pitch.
- **Format correction:** We specified that the deliverable should be a Markdown file. **Output:** AI saved the document as Markdown.
- **AI log revision:** We provided the professor's ADR format and a reusable prompt for recording AI use. **Output:** AI added this ADR section, separating the prompt/output record from the team's own rationale.

### Rationale — manual input required

This was just to make all of our ideas in a coordinated fashion.  No new ideas were given by AI here, just organized them in a good way. 
### Consequences

AI helped with structure, wording, and the pitch draft. The JACpot concept, feature ideas, and description of the team's alignment came from the supplied team notes and instructions. The AI draft does not replace missing raw meeting transcripts or firsthand user research; those limits are stated in this document. Before submitting, we need to review the draft, fill in the rationale above, and confirm that the wording matches what our team agreed to.

**Conversation record:** 
https://chatgpt.com/s/cx_6abfff491e108191bbc553e981c24a7a 


### Chris's Ai Usage below
