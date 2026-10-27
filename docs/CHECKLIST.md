# Checklist — taubakul-zhanibek-student-api

Student: Taubakul Zhanibek Nurlanuly

Legend: [x] done and verified, [ ] not done yet

## Part 1 — Linux administration

### Task 1.1 — Project structure
| Requirement | Done | Verified by | Evidence |
|---|---|---|---|
| Directories `scripts/ logs/ backups/ src/` created with terminal commands | [x] `mkdir -p` | `tree -a`, OK/MISSING loop | [task-1.1](evidence/task-1.1.txt) |
| Files `Dockerfile docker-compose.yml Jenkinsfile build.gradle README.md` | [x] `touch`, README has initial content | `tree -a`, loop | [task-1.1](evidence/task-1.1.txt) |
| Personal script `Taubakul_Zhanibek_system_report.sh` | [x] `scripts/` | loop → OK | [task-1.1](evidence/task-1.1.txt) |
| Empty log `Taubakul_Zhanibek_system_report.log` | [x] `touch`, size 0 before first run | `ls -l`, `wc -c` → 0 | [task-1.1](evidence/task-1.1.txt) |
| Directory tree displayed, all required files exist | [x] | `tree -a`, all 11 items OK | [task-1.1](evidence/task-1.1.txt) |

### Task 1.2 — System monitoring script
| Requirement | Done | Verified by | Evidence |
|---|---|---|---|
| Student's full name | [x] Taubakul Zhanibek Nurlanuly | script output / log | [task-1.2](evidence/task-1.2.txt) |
| Current date and time | [x] | output | [task-1.2](evidence/task-1.2.txt) |
| Current username | [x] `id -un` | output | [task-1.2](evidence/task-1.2.txt) |
| OS and kernel version | [x] `sw_vers` / `/etc/os-release`, `uname` | macOS + Ubuntu runs | [task-1.2](evidence/task-1.2.txt) |
| CPU and memory information | [x] `sysctl`, `vm_stat` / `lscpu`, `free -h` (fallback `/proc`) | macOS + Ubuntu runs | [task-1.2](evidence/task-1.2.txt) |
| Disk usage | [x] `df -h` | output | [task-1.2](evidence/task-1.2.txt) |
| Available disk space | [x] separate line "Available on /" | output | [task-1.2](evidence/task-1.2.txt) |
| Output saved to personal log | [x] `tee -a`, header with run time per run | 2 runs (project root and `~`) → 2 headers in log | [task-1.2](evidence/task-1.2.txt) |
| Extra: `$1` = log path, `-h/--help`, error on unknown option | [x] | `--help`, `--bad-option` → exit 1 | [task-1.2](evidence/task-1.2.txt) |
| Extra: macOS bash 3.2 + Linux compatible, shellcheck clean | [x] | `shellcheck`, `docker run ubuntu:24.04` | [task-1.2](evidence/task-1.2.txt) |

### Task 1.3 — Permissions and backup
| Requirement | Done | Verified by | Evidence |
|---|---|---|---|
| Script made executable | [x] `chmod +x` | `ls -l` → `-rwxr-xr-x`, `./script` runs | [task-1.3](evidence/task-1.3.txt) |
| Owner rwx, group/others read-only | [x] `chmod 744` | `ls -l` → `-rwxr--r--`, `stat` → 744 | [task-1.3](evidence/task-1.3.txt) |
| Backup of README.md with student name | [x] `backups/README_Taubakul_Zhanibek_backup.md` | `ls -l` | [task-1.3](evidence/task-1.3.txt) |
| Permissions verified with `ls -l` | [x] | `ls -l` | [task-1.3](evidence/task-1.3.txt) |
| Backup exists and has expected contents | [x] | `cat`, `diff`, `cmp` → identical, same SHA-256 | [task-1.3](evidence/task-1.3.txt) |

### Task 1.4 — Process and log analysis
| Requirement | Done | Verified by | Evidence |
|---|---|---|---|
| Display running processes | [x] `ps aux` | output (~800 processes) | [task-1.4](evidence/task-1.4.txt) |
| Identify a Java process | [x] PID 748 = Jenkins (`jenkins.war`, openjdk@21) | `ps aux \| grep -i '[j]ava'`, `lsof` → 127.0.0.1:8080 | [task-1.4](evidence/task-1.4.txt) |
| Extra: kill a process by PID | [x] `sleep 300 &` → `kill PID` | `ps -p` → gone | [task-1.4](evidence/task-1.4.txt) |
| Last five lines of the log | [x] `tail -n 5` | output | [task-1.4](evidence/task-1.4.txt) |
| Search log for surname | [x] `grep -n 'Taubakul'` | 3 matches (one per run) | [task-1.4](evidence/task-1.4.txt) |
| Results written to `Taubakul_Zhanibek_log_analysis.txt` | [x] `logs/Taubakul_Zhanibek_log_analysis.txt` | `cat`, `ls -l` | [task-1.4](evidence/task-1.4.txt) |
| Explain `>` vs `>>` | [x] demo on temp file (created, appended, overwritten, deleted) + explanation | `cat` after each step | [task-1.4](evidence/task-1.4.txt) |

## Application (stage 2 — base for Parts 2–4)
| Requirement | Done | Verified by | Evidence |
|---|---|---|---|
| Spring Boot project generated via Initializr (curl), Gradle Groovy DSL + wrapper | [x] Boot 4.1.2, Gradle 9.8.2, `gradlew` executable | `./gradlew --version`, `ls -l gradlew` | [stage-2](evidence/stage-2-app.txt) |
| Java 21 toolchain, no machine-specific paths in the project | [x] `JavaLanguageVersion.of(21)`, no `gradle.properties` | build log (Launcher JVM 21) | [stage-2](evidence/stage-2-app.txt) |
| Dependencies: Web, Data JPA, PostgreSQL, Validation, Actuator; H2 test-only | [x] `testRuntimeOnly 'com.h2database:h2'` | `build.gradle` | [stage-2](evidence/stage-2-app.txt) |
| Single executable jar (plain jar disabled) | [x] `jar { enabled = false }` | `ls -l build/libs` → 1 file | [stage-2](evidence/stage-2-app.txt) |
| Student entity with Bean Validation (`@NotBlank`, `@Email`, `@Min/@Max`), unique email | [x] | tests (400) + curl (400) | [stage-2](evidence/stage-2-app.txt) |
| Layers: entity → repository → service → controller + `@RestControllerAdvice` | [x] | code review, tests | — |
| POST 201 + Location | [x] | test + curl → 201, `Location: .../api/students/4` | [stage-2](evidence/stage-2-app.txt) |
| GET all / GET by id / 404 JSON | [x] | tests + curl → 200 / 200 / 404 | [stage-2](evidence/stage-2-app.txt) |
| DELETE 204 / 404 | [x] | tests + curl → 204, then 404 | [stage-2](evidence/stage-2-app.txt) |
| 400 with field list, 409 duplicate email, unified error JSON | [x] | tests + curl → 400 / 409 | [stage-2](evidence/stage-2-app.txt) |
| PUT postponed to `feature/taubakul-zhanibek-crud` (task 2.2) | [x] not implemented | curl PUT → 405 | [stage-2](evidence/stage-2-app.txt) |
| `/api/info` with author and version from `build.gradle` | [x] `buildInfo()` → `version 1.0.0` | test + curl | [stage-2](evidence/stage-2-app.txt) |
| Individual seed data, only if table is empty | [x] 3 students, first Zhanibek Taubakul | log "Seeded 3" → after restart "seeding skipped" | [stage-2](evidence/stage-2-app.txt) |
| Config via env vars, password without default, `.env.example` | [x] `application.yml`, `.env.example` | review; app started with exported vars | [stage-2](evidence/stage-2-app.txt) |
| Actuator health + info exposed | [x] | curl `/actuator/health` → UP | [stage-2](evidence/stage-2-app.txt) |
| Tests: H2 profile, MockMvc for every endpoint, service unit test, personalized data | [x] 16 tests (12 MockMvc, 3 unit, 1 context) | `./gradlew clean build` → 16/16 passed | [stage-2](evidence/stage-2-app.txt) |
| Live check on PostgreSQL 16, data persists after app restart | [x] temp container `tz-postgres-tmp` | `psql SELECT`, restart (new PID) → 4 rows | [stage-2](evidence/stage-2-app.txt) |
| Cleanup: app stopped, temp container removed, ports 5432/8081 free | [x] | `docker ps -a`, `lsof` | [stage-2](evidence/stage-2-app.txt) |
| README: stack, build/run, env vars, endpoint table (PUT planned) | [x] | `README.md` | — |

## Part 2 — Git and GitHub

### Task 2.1 — Repository initialization
| Requirement | Done | Verified by | Evidence |
|---|---|---|---|
| Git repository initialized in the project directory | [x] `git init -b main` | `git status` | [task-2.1](evidence/task-2.1.txt) |
| Git username and email configured | [x] global `user.name` / `user.email` | `git config --global --list \| grep user` | [task-2.1](evidence/task-2.1.txt) |
| `.gitignore` created | [x] build output, IDE, OS, `.env`/`*.env` (except `.env.example`), `*.log` (except Part 1 log) | `git check-ignore -v build/ .gradle/ .env ...` | [task-2.1](evidence/task-2.1.txt) |
| Extra: `.gitattributes` (LF for `gradlew`, `*.sh`; CRLF for `*.bat`) | [x] | `git ls-files --eol` | [task-2.1](evidence/task-2.1.txt) |
| Nothing unwanted staged | [x] | `git status --short`, check for build/.gradle/.env | [task-2.1](evidence/task-2.1.txt) |
| First commit "Initial commit: Taubakul Zhanibek Student API" | [x] `7ad0213` | `git log` | [task-2.1](evidence/task-2.1.txt) |

### Task 2.2 — Branching and feature development
| Requirement | Done | Verified by | Evidence |
|---|---|---|---|
| Branch `feature/taubakul-zhanibek-crud` | [x] | `git branch` | [task-2.2](evidence/task-2.2.txt) |
| Implement one CRUD operation | [x] `PUT /api/students/{id}`: 200 / 404 / 400 / 409, own email allowed | 6 new tests (5 MockMvc + 1 unit) | [task-2.2](evidence/task-2.2.txt) |
| README updated with name and description of the change | [x] PUT in endpoint table, "Changelog" section | `git diff` / commit `e7121bd` | [task-2.2](evidence/task-2.2.txt) |
| At least two meaningful commits | [x] 3 commits: `110b2bb`, `150800a`, `e7121bd` | `git log main..feature/...` | [task-2.2](evidence/task-2.2.txt) |
| Merge into main | [x] `git merge --no-ff` → merge commit `4ccde2a` | graph; build on main → 22 tests, 0 failures | [task-2.2](evidence/task-2.2.txt) |

### Task 2.3 — Conflict resolution
| Requirement | Done | Verified by | Evidence |
|---|---|---|---|
| Branch `feature/taubakul-zhanibek-documentation` | [x] from updated main | `git log --graph` | [task-2.3](evidence/task-2.3.txt) |
| Same README line changed on two branches | [x] line 5 (description): `2ff6d48` on branch, `910575c` on main | `git diff` per commit | [task-2.3](evidence/task-2.3.txt) |
| Merge → conflict | [x] `CONFLICT (content): Merge conflict in README.md` | merge output, `git status`, markers | [task-2.3](evidence/task-2.3.txt) |
| Conflict resolved manually and committed | [x] combined line, merge commit `cf306fd` | `grep` for markers → none | [task-2.3](evidence/task-2.3.txt) |
| Final Git history shown | [x] | `git log --oneline --graph --decorate --all` | [task-2.3](evidence/task-2.3.txt) |

### Task 2.4 — Remote repository and version tags
| Requirement | Done | Verified by | Evidence |
|---|---|---|---|
| Personal GitHub repository `taubakul-zhanibek-student-api` | [x] public, description set | `gh repo view` | [task-2.4](evidence/task-2.4.txt) |
| Push main and feature branches | [x] `git push -u origin` ×3 | `git ls-remote --heads origin`, `gh api .../branches` | [task-2.4](evidence/task-2.4.txt) |
| Remote URL, commit history, branches shown | [x] | `git remote -v`, `git branch -a`, `gh api .../commits` | [task-2.4](evidence/task-2.4.txt) |
| Commits linked to GitHub account D4rk26, no AI mentions | [x] all commits `author.login=D4rk26` (re-checked after the history rewrite, see [history-rewrite](evidence/history-rewrite.txt)) | `gh api`, `git log \| grep` → empty | [task-2.4](evidence/task-2.4.txt) |
| Tag `v1.0-taubakul-zhanibek` | [ ] **planned: stage 6** — the tag goes on the final commit so v1.0 includes Dockerfile, Compose and Jenkinsfile and matches the Docker image tag | — | — |
| Explain why `.env`, passwords, DB credentials must not be committed | [x] | written explanation | [task-2.4](evidence/task-2.4.txt) |

## Part 3 — Docker and Docker Compose
- [ ] 3.1 Dockerfile
- [ ] 3.2 Docker Compose and PostgreSQL
- [ ] 3.3 Container management
- [ ] 3.4 Individual API testing

## Part 4 — Jenkins CI/CD
- [ ] 4.1 Jenkins setup
- [ ] 4.2 Personalized Jenkinsfile
- [ ] 4.3 Pipeline execution and troubleshooting
- [ ] 4.4 Automation
