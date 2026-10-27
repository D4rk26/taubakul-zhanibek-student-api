# Checklist — taubakul-zhanibek-student-api

Student: **Taubakul Zhanibek Nurlanuly** · GitHub: [D4rk26/taubakul-zhanibek-student-api](https://github.com/D4rk26/taubakul-zhanibek-student-api) · Release tag: `v1.0-taubakul-zhanibek`

Every requirement of the midterm task → status → where it is in the repository → evidence file → how to show it live.
All requirements are done (`[x]`). Live commands are run from the project root; Jenkins at http://127.0.0.1:8080 (user `zhanibek`).
`J` = `~/projects/jenkins-tools/jcli` (jenkins-cli with the API token).

---

## Part 1 — Linux administration

### Task 1.1 — Project structure
| Requirement | Status | Where | Evidence | Show live |
|---|---|---|---|---|
| Directories `scripts/ logs/ backups/ src/` created with terminal commands | [x] `mkdir -p` | repo root | [task-1.1](evidence/task-1.1.txt) | `tree -L 1 -a -I '.git\|build\|.gradle'` |
| Files `Dockerfile docker-compose.yml Jenkinsfile build.gradle README.md` | [x] `touch` (filled in later stages) | repo root | [task-1.1](evidence/task-1.1.txt) | `ls -l Dockerfile docker-compose.yml Jenkinsfile build.gradle README.md` |
| Personal script `Taubakul_Zhanibek_system_report.sh` | [x] | `scripts/` | [task-1.1](evidence/task-1.1.txt) | `ls -l scripts/` |
| Empty log `Taubakul_Zhanibek_system_report.log` | [x] size 0 before the first run | `logs/` | [task-1.1](evidence/task-1.1.txt) | evidence shows `wc -c` → 0 |
| Display the tree, verify all files exist | [x] `tree -a` + OK/MISSING loop | — | [task-1.1](evidence/task-1.1.txt) | `tree -a -I '.git\|build\|.gradle\|src'` |

### Task 1.2 — System monitoring script
| Requirement | Status | Where | Evidence | Show live |
|---|---|---|---|---|
| Full name | [x] Taubakul Zhanibek Nurlanuly | `scripts/Taubakul_Zhanibek_system_report.sh` | [task-1.2](evidence/task-1.2.txt) | `./scripts/Taubakul_Zhanibek_system_report.sh` |
| Date and time, username | [x] `date`, `id -un` | same | [task-1.2](evidence/task-1.2.txt) | same |
| OS and kernel version | [x] `sw_vers` / `/etc/os-release`, `uname` | same | [task-1.2](evidence/task-1.2.txt) | same |
| CPU and memory | [x] `sysctl`, `vm_stat` (macOS) / `lscpu`, `free -h` (Linux) | same | [task-1.2](evidence/task-1.2.txt) | same; Linux: `docker run --rm -v "$PWD":/w -w /w ubuntu:24.04 bash scripts/Taubakul_Zhanibek_system_report.sh /tmp/t.log` |
| Disk usage, available space | [x] `df -h`, line "Available on /" | same | [task-1.2](evidence/task-1.2.txt) | same |
| Output saved to the personal log | [x] `tee -a`, header per run | `logs/Taubakul_Zhanibek_system_report.log` | [task-1.2](evidence/task-1.2.txt) | `grep -n "run at" logs/Taubakul_Zhanibek_system_report.log` |
| Extra: argument `$1`, `--help`, shellcheck clean | [x] | same | [task-1.2](evidence/task-1.2.txt) | `./scripts/Taubakul_Zhanibek_system_report.sh --help` |

### Task 1.3 — Permissions and backup
| Requirement | Status | Where | Evidence | Show live |
|---|---|---|---|---|
| Script executable | [x] `chmod +x` | `scripts/` | [task-1.3](evidence/task-1.3.txt) | `ls -l scripts/` |
| Owner rwx, group/others read-only | [x] `chmod 744` → `-rwxr--r--` (Git stores only the x-bit: `100755`) | same | [task-1.3](evidence/task-1.3.txt) | `stat -f '%Sp %Lp %N' scripts/Taubakul_Zhanibek_system_report.sh` |
| Backup of README.md with my name | [x] | `backups/README_Taubakul_Zhanibek_backup.md` | [task-1.3](evidence/task-1.3.txt) | `ls -l backups/` |
| Verify permissions with `ls -l` | [x] | — | [task-1.3](evidence/task-1.3.txt) | `ls -l scripts/` |
| Backup exists, expected contents | [x] `cmp` → identical, same SHA-256 (backup = README at stage 1) | — | [task-1.3](evidence/task-1.3.txt) | `cat backups/README_Taubakul_Zhanibek_backup.md` |

### Task 1.4 — Process and log analysis
| Requirement | Status | Where | Evidence | Show live |
|---|---|---|---|---|
| Running processes, Java process | [x] Jenkins (`java -jar jenkins.war`, openjdk@21, 127.0.0.1:8080) | — | [task-1.4](evidence/task-1.4.txt) | `ps aux \| grep -i '[j]ava' \| cut -c1-150` |
| Extra: kill by PID | [x] `sleep 300 &` → `kill PID` | — | [task-1.4](evidence/task-1.4.txt) | `sleep 300 & kill $!` |
| Last five lines of the log | [x] `tail -n 5` | `logs/` | [task-1.4](evidence/task-1.4.txt) | `tail -n 5 logs/Taubakul_Zhanibek_system_report.log` |
| Search the log for my surname | [x] `grep -n 'Taubakul'` | `logs/` | [task-1.4](evidence/task-1.4.txt) | `grep -n Taubakul logs/Taubakul_Zhanibek_system_report.log` |
| Results in `Taubakul_Zhanibek_log_analysis.txt` | [x] | `logs/Taubakul_Zhanibek_log_analysis.txt` | [task-1.4](evidence/task-1.4.txt) | `cat logs/Taubakul_Zhanibek_log_analysis.txt` |
| Explain `>` vs `>>` | [x] demo + explanation (`>` overwrites, `>>` appends) | — | [task-1.4](evidence/task-1.4.txt) | `echo a > /tmp/r; echo b >> /tmp/r; cat /tmp/r` |

**Part 1 Evidence** (terminal demonstration, script source code, output log, permissions and backup file): [task-1.1](evidence/task-1.1.txt)–[task-1.4](evidence/task-1.4.txt), `scripts/Taubakul_Zhanibek_system_report.sh`, `logs/`, `backups/`.

---

## Application (base for Parts 2–4)
| Requirement | Status | Where | Evidence | Show live |
|---|---|---|---|---|
| Spring Boot CRUD "Student API", Gradle wrapper, Java 21 | [x] Boot 4.1.2, Gradle 9.8.2, version 1.0.1 | `build.gradle`, `src/` | [stage-2](evidence/stage-2-app.txt) | `./gradlew clean build` (JAVA_HOME = JDK 21) |
| CRUD `/api/students`: POST 201, GET, GET by id, PUT, DELETE 204; 404/400/409 JSON errors | [x] | `controller/`, `service/`, `exception/` | [stage-2](evidence/stage-2-app.txt), [task-3.4](evidence/task-3.4.txt) | `./scripts/api_test.sh` |
| PostgreSQL, configuration via environment variables, no secrets in code | [x] `application.yml`, `.env.example` | `src/main/resources/` | [stage-2](evidence/stage-2-app.txt) | `grep -n DB_ src/main/resources/application.yml` |
| Tests (H2): 17 MockMvc + 4 unit + 1 context = 22 | [x] | `src/test/` | [stage-2](evidence/stage-2-app.txt), [task-4.2](evidence/task-4.2.txt) | Jenkins → main → Test Result |
| My name and individual test data | [x] `/api/info` author, seed student Zhanibek Taubakul | `InfoController`, `DataInitializer` | [task-3.2](evidence/task-3.2.txt) | `curl -s localhost:8081/api/info` |

---

## Part 2 — Git and GitHub

### Task 2.1 — Repository initialization
| Requirement | Status | Where | Evidence | Show live |
|---|---|---|---|---|
| Git repository initialized | [x] `git init -b main` | `.git/` | [task-2.1](evidence/task-2.1.txt) | `git status` |
| Username and email configured | [x] global `Taubakul Zhanibek` / `taubakulzhanibek@gmail.com` | — | [task-2.1](evidence/task-2.1.txt) | `git config --global --list \| grep user` |
| `.gitignore` | [x] build output, IDE, OS, `.env`/`*.env` except `.env.example`, logs except the Part 1 log | `.gitignore` | [task-2.1](evidence/task-2.1.txt) | `git check-ignore -v build/ .env` |
| First commit "Initial commit: Taubakul Zhanibek Student API" | [x] `7ad0213` | history | [task-2.1](evidence/task-2.1.txt), [history-rewrite](evidence/history-rewrite.txt) | `git log --oneline \| tail -1` |

### Task 2.2 — Branching and feature development
| Requirement | Status | Where | Evidence | Show live |
|---|---|---|---|---|
| Branch `feature/taubakul-zhanibek-crud` | [x] | GitHub branch | [task-2.2](evidence/task-2.2.txt) | `git branch -a` |
| Implement one CRUD operation | [x] `PUT /api/students/{id}` (200/400/404/409) + 6 tests | `StudentService.update`, `StudentController.update` | [task-2.2](evidence/task-2.2.txt) | `git show --stat 110b2bb` |
| README with my name and the change | [x] endpoint table + Changelog | `README.md` | [task-2.2](evidence/task-2.2.txt) | `git show e7121bd` |
| At least two meaningful commits | [x] `110b2bb`, `150800a`, `e7121bd` | history | [task-2.2](evidence/task-2.2.txt) | `git log --oneline 7ad0213..e7121bd` |
| Merge into main | [x] `--no-ff` → `4ccde2a` | history | [task-2.2](evidence/task-2.2.txt) | `git log --oneline --graph 7ad0213..4ccde2a` |

### Task 2.3 — Conflict resolution
| Requirement | Status | Where | Evidence | Show live |
|---|---|---|---|---|
| Branch `feature/taubakul-zhanibek-documentation` | [x] | GitHub branch | [task-2.3](evidence/task-2.3.txt) | `git branch -a` |
| Same README line changed on two branches | [x] line 5: `2ff6d48` (branch) vs `910575c` (main) | history | [task-2.3](evidence/task-2.3.txt) | `git show 2ff6d48 910575c --stat` |
| Merge → conflict, resolved manually | [x] markers shown, combined line | `README.md` line 5 | [task-2.3](evidence/task-2.3.txt) | `git show cf306fd` |
| Resolution committed | [x] `cf306fd` | history | [task-2.3](evidence/task-2.3.txt) | `git log -1 cf306fd` |
| Final history shown | [x] | — | [task-2.3](evidence/task-2.3.txt) | `git log --oneline --graph --decorate --all` |

### Task 2.4 — Remote repository and version tags
| Requirement | Status | Where | Evidence | Show live |
|---|---|---|---|---|
| Personal GitHub repository `taubakul-zhanibek-student-api` | [x] public, description set | GitHub | [task-2.4](evidence/task-2.4.txt) | `gh repo view D4rk26/taubakul-zhanibek-student-api` |
| Push main and feature branches | [x] main + 5 branches (crud, documentation, docker, jenkins, test) | GitHub | [task-2.4](evidence/task-2.4.txt) | `git ls-remote --heads origin` |
| Tag `v1.0-taubakul-zhanibek` | [x] annotated tag on the final commit of the repository, pushed to GitHub. Created last, so v1.0 contains Docker and Jenkins and matches the image tag; its output is kept outside the repository so the tag stays on the last commit | GitHub → Tags | — | `git tag -n`, `git show v1.0-taubakul-zhanibek --stat`, `git ls-remote --tags origin` |
| Remote URL, history, branches, tag shown | [x] | — | [task-2.4](evidence/task-2.4.txt) | `git remote -v; git branch -a; git log --oneline --graph --decorate --all` |
| Explain why `.env`, passwords, DB credentials must not be committed | [x] | — | [task-2.4](evidence/task-2.4.txt) | `git check-ignore -v .env` |
| Commits linked to D4rk26, no co-author / generated-by trailers | [x] all commits | — | [task-2.4](evidence/task-2.4.txt), [final-audit](evidence/final-audit.txt) | `gh api repos/D4rk26/taubakul-zhanibek-student-api/commits --jq '.[].author.login' \| sort \| uniq -c` |

**Part 2 Evidence** (GitHub repository, commit history, branch merge, conflict resolution, tag): [task-2.1](evidence/task-2.1.txt)–[task-2.4](evidence/task-2.4.txt), the GitHub repository, merges `4ccde2a` and `cf306fd`, tag `v1.0-taubakul-zhanibek`. The history was rewritten once to remove local temporary paths from evidence files: [history-rewrite](evidence/history-rewrite.txt).

---

## Part 3 — Docker and Docker Compose

### Task 3.1 — Dockerfile
| Requirement | Status | Where | Evidence | Show live |
|---|---|---|---|---|
| Multi-stage Dockerfile | [x] `build` → runtime | `Dockerfile` | [task-3.1](evidence/task-3.1.txt) | `cat Dockerfile` |
| Official JDK image, build with Gradle | [x] `eclipse-temurin:21-jdk`, `./gradlew bootJar` (tests run in Jenkins) | `Dockerfile` | [task-3.1](evidence/task-3.1.txt) | `docker history taubakul-zhanibek-student-api:v1.0-taubakul-zhanibek` |
| Java runtime image, copy the jar | [x] `eclipse-temurin:21-jre`, `COPY --from=build`, non-root user `app` | `Dockerfile` | [task-3.1](evidence/task-3.1.txt) | `docker compose exec app whoami` |
| Expose the configured port | [x] `EXPOSE 8080` | `Dockerfile` | [task-3.1](evidence/task-3.1.txt) | `docker image inspect taubakul-zhanibek-student-api:v1.0-taubakul-zhanibek --format '{{json .Config.ExposedPorts}}'` |
| Individual image name and tag | [x] `taubakul-zhanibek-student-api:v1.0-taubakul-zhanibek` | — | [task-3.1](evidence/task-3.1.txt) | `docker images taubakul-zhanibek-student-api` |
| Image size and creation details | [x] 553 MB (a single-stage image would be 1321 MB) | — | [task-3.1](evidence/task-3.1.txt) | `docker image inspect taubakul-zhanibek-student-api:v1.0-taubakul-zhanibek --format '{{.Id}} {{.Created}} {{.Size}}'` |

### Task 3.2 — Docker Compose and PostgreSQL
| Requirement | Status | Where | Evidence | Show live |
|---|---|---|---|---|
| App service + PostgreSQL service | [x] `app`, `db` (postgres:16-alpine) | `docker-compose.yml` | [task-3.2](evidence/task-3.2.txt) | `docker compose ps` |
| Environment variables for DB config | [x] from `.env` (git-ignored), `.env.example` | `docker-compose.yml`, `.env.example` | [task-3.2](evidence/task-3.2.txt) | `docker compose exec app printenv DB_HOST DB_NAME` |
| Named volume, dedicated network | [x] `taubakul-zhanibek-pgdata`, `taubakul-zhanibek-net` | `docker-compose.yml` | [task-3.2](evidence/task-3.2.txt) | `docker volume ls; docker network ls \| grep taubakul` |
| Health check for PostgreSQL | [x] `pg_isready`, app waits for `service_healthy` | `docker-compose.yml` | [task-3.2](evidence/task-3.2.txt) | `docker compose ps` → `(healthy)` |
| Personalized Compose project name | [x] `taubakul-zhanibek-student-api` | `docker-compose.yml` | [task-3.2](evidence/task-3.2.txt) | `docker compose ls` |
| Both containers running | [x] | — | [task-3.2](evidence/task-3.2.txt) | `docker compose ps` |
| App uses the service name, not localhost | [x] `DB_HOST=db` → `jdbc:postgresql://db:5432/studentdb` | `docker-compose.yml` | [task-3.2](evidence/task-3.2.txt) | `docker compose exec app getent hosts db` |

### Task 3.3 — Container management
| Requirement | Status | Where | Evidence | Show live |
|---|---|---|---|---|
| List running containers | [x] | — | [task-3.3](evidence/task-3.3.txt) | `docker ps` |
| Application logs | [x] | — | [task-3.3](evidence/task-3.3.txt) | `docker compose logs app --tail 20` |
| Inspect container configuration | [x] password masked | — | [task-3.3](evidence/task-3.3.txt) | `docker inspect taubakul-zhanibek-app --format '{{.Config.Image}} {{.State.Status}} {{json .NetworkSettings.Ports}}'` |
| Execute a command inside the app container | [x] `whoami`, `java -version`, `curl` | — | [task-3.3](evidence/task-3.3.txt) | `docker compose exec app java -version` |
| Stop and restart the app service | [x] stop → Exited (143) → start/restart → 200 | — | [task-3.3](evidence/task-3.3.txt) | `docker compose restart app` |
| Rebuild after changing the app version | [x] 1.0.0 → 1.0.1 (`20e10f5`), new Image ID, `/api/info` → 1.0.1 | `build.gradle` | [task-3.3](evidence/task-3.3.txt) | `curl -s localhost:8081/api/info` |
| PostgreSQL records persist after restart | [x] `down` (no `-v`) / `up -d` → record still there | volume | [task-3.3](evidence/task-3.3.txt) | `docker compose exec db sh -c 'psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "select id,email from students"'` |

### Task 3.4 — Individual API testing
| Requirement | Status | Where | Evidence | Show live |
|---|---|---|---|---|
| Create a student with my name | [x] 201 | `scripts/api_test.sh` | [task-3.4](evidence/task-3.4.txt) | `./scripts/api_test.sh` |
| Get all / get by id | [x] 200 / 200 | same | [task-3.4](evidence/task-3.4.txt) | same |
| Update | [x] 200 (PUT) | same | [task-3.4](evidence/task-3.4.txt) | same |
| Delete | [x] 204 | same | [task-3.4](evidence/task-3.4.txt) | same |
| Nonexistent student | [x] 404 JSON | same | [task-3.4](evidence/task-3.4.txt) | same |
| Personalized URL, evidence of responses; Postman | [x] `http://localhost:8081`, 6/6 PASS; Postman collection | `docs/postman/` | [task-3.4](evidence/task-3.4.txt) | Postman → Import `docs/postman/Taubakul_Zhanibek_Student_API.postman_collection.json` → Run |

---

## Part 4 — Jenkins CI/CD
Jenkins configuration as code: `jenkins/plugins.txt`, `jenkins/casc.yaml`, `jenkins/credentials.template.yaml` (no secrets).

### Task 4.1 — Jenkins setup
| Requirement | Status | Where | Evidence | Show live |
|---|---|---|---|---|
| Job `Taubakul_Zhanibek_Student_API` | [x] Multibranch Pipeline (Job DSL in casc.yaml) | `jenkins/casc.yaml` | [task-4.1](evidence/task-4.1.txt) | Jenkins → job; `$J list-jobs` |
| Uses my GitHub repository | [x] all branches discovered | `jenkins/casc.yaml` | [task-4.1](evidence/task-4.1.txt) | job → Scan Repository Log |
| JDK, Gradle wrapper, Docker-capable agent | [x] `jdk-21`, `./gradlew`, built-in node with label `docker`, Docker CLI in PATH | `jenkins/casc.yaml`, `Jenkinsfile` | [task-4.1](evidence/task-4.1.txt) | Manage Jenkins → Nodes / Tools |
| Credentials in Jenkins Credentials | [x] `github-token` (the token was never written to disk) | `jenkins/credentials.template.yaml` | [task-4.1](evidence/task-4.1.txt) | `$J list-credentials system::system::jenkins` |
| Jenkins checks out the source | [x] | — | [task-4.1](evidence/task-4.1.txt) | any build → Console Output → Checkout |
| Extra: setup without GUI, single admin `zhanibek` | [x] plugin manager, wizard off, JCasC; wizard user `admin` removed | — | [task-4.1](evidence/task-4.1.txt), [final-audit](evidence/final-audit.txt) | `$J who-am-i` |

### Task 4.2 — Personalized Jenkinsfile
| Requirement | Status | Where | Evidence | Show live |
|---|---|---|---|---|
| Standard `Jenkinsfile` | [x] declarative, linter OK | `Jenkinsfile` | [task-4.2](evidence/task-4.2.txt) | `$J declarative-linter < Jenkinsfile` |
| Stages Checkout, Build, Test, Docker Build, Verify | [x] | `Jenkinsfile` | [task-4.2](evidence/task-4.2.txt) | job → main → Stage View |
| My image name | [x] `taubakul-zhanibek-student-api:v1.0-taubakul-zhanibek` + `:build-N` | `Jenkinsfile` | [task-4.2](evidence/task-4.2.txt) | console → Docker Build / Verify |
| Stops when build or tests fail | [x] | `Jenkinsfile` | [task-4.3](evidence/task-4.3.txt) | test branch build #1 |
| My name in the build log | [x] `#N Taubakul Zhanibek`, "VERIFIED by Taubakul Zhanibek" | `Jenkinsfile` | [task-4.2](evidence/task-4.2.txt) | console output |
| Green feature build → merge `--no-ff` → green main | [x] merge `c824aef`, main #2 SUCCESS | history | [task-4.2](evidence/task-4.2.txt) | job → main → build history |

### Task 4.3 — Pipeline execution and troubleshooting
| Requirement | Status | Where | Evidence | Show live |
|---|---|---|---|---|
| Successful run | [x] | — | [task-4.2](evidence/task-4.2.txt) | main → last build |
| Controlled error in own test branch | [x] `a259ca9` on `test/taubakul-zhanibek-pipeline-failure` | GitHub branch | [task-4.3](evidence/task-4.3.txt) | `git show a259ca9` |
| Failed pipeline | [x] #1: Test FAILED, Docker Build and Verify skipped | — | [task-4.3](evidence/task-4.3.txt) | test branch → #1 → Console / Test Result |
| Fix and rerun | [x] `38dc247` → #2 SUCCESS | — | [task-4.3](evidence/task-4.3.txt) | test branch → #2 |
| Explain the failure | [x] | — | [task-4.3](evidence/task-4.3.txt) | — |

### Task 4.4 — Automation
| Requirement | Status | Where | Evidence | Show live |
|---|---|---|---|---|
| Webhook or polling | [x] polling: periodic multibranch scan every 2 min (a webhook cannot reach 127.0.0.1) | `jenkins/casc.yaml` | [task-4.4](evidence/task-4.4.txt) | job → Configure → Scan Repository Triggers |
| New commit starts the pipeline automatically | [x] `a92c42d` → main #3, cause "Branch indexing" | — | [task-4.4](evidence/task-4.4.txt) | any build → "Branch indexing" |
| Build history and console output | [x] | — | [task-4.4](evidence/task-4.4.txt) | job → branches → history |
| Explain CI vs Continuous Deployment | [x] CI vs Delivery vs Deployment | — | [task-4.4](evidence/task-4.4.txt) | — |

---

## Individualization rules
| Resource | Requirement | Mine | Show live |
|---|---|---|---|
| Linux script | `Surname_Name_system_report.sh` | `Taubakul_Zhanibek_system_report.sh` | `ls scripts/` |
| Log and analysis files | include surname and name | `Taubakul_Zhanibek_system_report.log`, `Taubakul_Zhanibek_log_analysis.txt` | `ls logs/` |
| Backup | surname and name in the filename | `README_Taubakul_Zhanibek_backup.md` | `ls backups/` |
| Git repository | `surname-name-student-api` | `taubakul-zhanibek-student-api` | `git remote -v` |
| Feature branches | include surname and name | `feature/taubakul-zhanibek-crud`, `feature/taubakul-zhanibek-documentation`, `feature/taubakul-zhanibek-docker`, `feature/taubakul-zhanibek-jenkins`, `test/taubakul-zhanibek-pipeline-failure` | `git branch -a` |
| Git commits | at least three meaningful personalized messages | e.g. "Initial commit: Taubakul Zhanibek Student API", "Add multi-stage Dockerfile for Taubakul Zhanibek Student API", "Introduce failing test to verify CI (Taubakul Zhanibek)" | `git log --all --oneline \| grep "Taubakul Zhanibek"` |
| Docker image | `surname-name-student-api` | `taubakul-zhanibek-student-api` | `docker images taubakul-zhanibek-student-api` |
| Docker tag | include student identifier | `v1.0-taubakul-zhanibek` (+ `build-N` from Jenkins) | same |
| Container and Compose project | personal project name | containers `taubakul-zhanibek-app`, `taubakul-zhanibek-postgres`; project `taubakul-zhanibek-student-api` | `docker compose ps` |
| Jenkins job | `Surname_Name_Student_API` | `Taubakul_Zhanibek_Student_API` | Jenkins → job list |
| Jenkins console | print student's name | `#N Taubakul Zhanibek`, `Pipeline of Taubakul Zhanibek Nurlanuly`, "VERIFIED by Taubakul Zhanibek" | any build → Console Output |
| Spring Boot application | display my name and individual test data | `GET /api/info` → author "Taubakul Zhanibek Nurlanuly"; seed student Zhanibek Taubakul; tests with my name | `curl -s localhost:8081/api/info` |

## Final audit
A clean clone builds (22 tests), `.env.example` → `docker compose config` is valid, scripts and `gradlew` are stored as `100755`, no secret values and no generated-by mentions in the whole history: [final-audit](evidence/final-audit.txt).
