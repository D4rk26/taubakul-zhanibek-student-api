#!/bin/bash
#
# api_test.sh
# End-to-end test of the Student API (taubakul-zhanibek-student-api, author: Taubakul Zhanibek).
# Runs create -> list -> get -> update -> delete -> get missing, checks HTTP codes,
# prints PASS/FAIL per step and exits with 1 if any step failed.
#
# Usage: api_test.sh [BASE_URL]      (default: http://localhost:8081)

set -u

BASE_URL="${1:-http://localhost:8081}"
BASE_URL="${BASE_URL%/}"
API="$BASE_URL/api/students"

PASSED=0
FAILED=0
RESPONSE_BODY=""
RESPONSE_CODE=""

command -v curl >/dev/null 2>&1 || { echo "ERROR: curl is required" >&2; exit 1; }
HAS_JQ=0
command -v jq >/dev/null 2>&1 && HAS_JQ=1

# Pretty-print JSON with jq when available, otherwise print as is
print_body() {
    if [ -z "$1" ]; then
        echo "(empty body)"
    elif [ "$HAS_JQ" -eq 1 ]; then
        echo "$1" | jq . 2>/dev/null || echo "$1"
    else
        echo "$1"
    fi
}

# Extract the numeric "id" field from a JSON object
extract_id() {
    if [ "$HAS_JQ" -eq 1 ]; then
        echo "$1" | jq -r '.id // empty'
    else
        echo "$1" | sed -n 's/.*"id":\([0-9][0-9]*\).*/\1/p'
    fi
}

# request STEP_NAME EXPECTED_CODE METHOD URL [JSON_BODY]
request() {
    local name="$1" expected="$2" method="$3" url="$4" data="${5:-}"
    local output

    echo "------------------------------------------------------------"
    echo "$name"
    echo "$method $url"
    # On a connection error curl fails; code 000 means "no HTTP response"
    if [ -n "$data" ]; then
        output=$(curl -s -w '\n%{http_code}' -X "$method" "$url" \
            -H 'Content-Type: application/json' -d "$data") || output=$'\n000'
    else
        output=$(curl -s -w '\n%{http_code}' -X "$method" "$url") || output=$'\n000'
    fi

    # Last line is the HTTP code, everything before it is the body
    RESPONSE_CODE="${output##*$'\n'}"
    RESPONSE_BODY="${output%$'\n'*}"

    echo "HTTP $RESPONSE_CODE (expected $expected)"
    print_body "$RESPONSE_BODY"

    if [ "$RESPONSE_CODE" = "$expected" ]; then
        echo "=> PASS"
        PASSED=$((PASSED + 1))
    else
        echo "=> FAIL"
        FAILED=$((FAILED + 1))
    fi
}

echo "Student API test — Taubakul Zhanibek"
echo "Base URL: $BASE_URL"

# Unique email, so the script can be run many times without a 409 conflict
EMAIL="zhanibek.taubakul.$(date +%s)@example.com"

request "1. Create a student" 201 POST "$API" \
    "{\"firstName\":\"Zhanibek\",\"lastName\":\"Taubakul\",\"email\":\"$EMAIL\",\"groupName\":\"DevOps-2026\",\"course\":3}"
STUDENT_ID=$(extract_id "$RESPONSE_BODY")

if [ -z "$STUDENT_ID" ]; then
    echo "ERROR: could not read the id of the created student, the remaining steps need it" >&2
    exit 1
fi
echo "Created student id: $STUDENT_ID"

request "2. Get all students" 200 GET "$API"

request "3. Get the student by id" 200 GET "$API/$STUDENT_ID"

request "4. Update the student (PUT)" 200 PUT "$API/$STUDENT_ID" \
    "{\"firstName\":\"Zhanibek\",\"lastName\":\"Taubakul\",\"email\":\"$EMAIL\",\"groupName\":\"DevOps-2027\",\"course\":4}"

request "5. Delete the student" 204 DELETE "$API/$STUDENT_ID"

request "6. Get the deleted (nonexistent) student" 404 GET "$API/$STUDENT_ID"

echo "============================================================"
echo "Result: $PASSED passed, $FAILED failed"
if [ "$FAILED" -eq 0 ]; then
    echo "ALL TESTS PASSED"
    exit 0
fi
echo "SOME TESTS FAILED"
exit 1
