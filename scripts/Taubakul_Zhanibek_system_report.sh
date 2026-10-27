#!/bin/bash
#
# Taubakul_Zhanibek_system_report.sh
# System monitoring report for the taubakul-zhanibek-student-api project.
#
# Prints student name, date/time, user, OS/kernel, CPU, memory and disk
# information to the screen and appends it to a log file.
# Works on macOS (bash 3.2) and Linux.
#
# Usage: Taubakul_Zhanibek_system_report.sh [LOG_FILE]
#        Taubakul_Zhanibek_system_report.sh -h | --help

set -u

STUDENT_NAME="Taubakul Zhanibek Nurlanuly"

# Resolve the project root from the script location so the default log path
# works no matter which directory the script is started from.
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
DEFAULT_LOG="$PROJECT_DIR/logs/Taubakul_Zhanibek_system_report.log"

usage() {
    cat <<EOF
Usage: $(basename "$0") [LOG_FILE]

Collects system information and appends it to LOG_FILE.

Arguments:
  LOG_FILE     Path to the log file (default: logs/Taubakul_Zhanibek_system_report.log
               in the project directory)
  -h, --help   Show this help and exit
EOF
}

error() {
    echo "ERROR: $*" >&2
    exit 1
}

section() {
    echo
    echo "--- $1 ---"
}

report_header() {
    echo "=================================================================="
    echo " System report — run at $(date '+%Y-%m-%d %H:%M:%S %Z')"
    echo "=================================================================="
}

report_student() {
    section "Student"
    echo "Full name:     $STUDENT_NAME"
}

report_datetime() {
    section "Date and time"
    echo "Current date:  $(date '+%Y-%m-%d')"
    echo "Current time:  $(date '+%H:%M:%S %Z')"
}

report_user() {
    section "User"
    echo "Current user:  $(id -un 2>/dev/null || whoami)"
    echo "Hostname:      $(hostname 2>/dev/null || uname -n)"
}

report_os() {
    section "Operating system and kernel"
    if [ "$OS_TYPE" = "Darwin" ]; then
        echo "OS:            $(sw_vers -productName) $(sw_vers -productVersion) (build $(sw_vers -buildVersion))"
    elif [ -r /etc/os-release ]; then
        # PRETTY_NAME is set by /etc/os-release on all mainstream distros
        # shellcheck source=/dev/null
        echo "OS:            $(. /etc/os-release && echo "${PRETTY_NAME:-$NAME}")"
    else
        echo "OS:            $OS_TYPE"
    fi
    echo "Kernel:        $(uname -s) $(uname -r)"
    echo "Architecture:  $(uname -m)"
}

report_cpu() {
    section "CPU"
    if [ "$OS_TYPE" = "Darwin" ]; then
        echo "Model:         $(sysctl -n machdep.cpu.brand_string)"
        echo "CPU cores:     $(sysctl -n hw.ncpu)"
    elif command -v lscpu >/dev/null 2>&1; then
        lscpu | grep -E '^(Model name|Architecture|CPU\(s\)|Thread\(s\) per core|Core\(s\) per socket):'
    else
        echo "Model:         $(grep -m1 -E 'model name|Hardware' /proc/cpuinfo | cut -d: -f2- | sed 's/^ *//')"
        echo "CPU cores:     $(grep -c '^processor' /proc/cpuinfo)"
    fi
}

report_memory() {
    section "Memory"
    if [ "$OS_TYPE" = "Darwin" ]; then
        # macOS has no 'free': total from sysctl, free/inactive pages from vm_stat
        local total_bytes page_size free_pages inactive_pages
        total_bytes=$(sysctl -n hw.memsize)
        page_size=$(sysctl -n hw.pagesize)
        free_pages=$(vm_stat | awk '/Pages free/ {gsub("\\.", "", $3); print $3}')
        inactive_pages=$(vm_stat | awk '/Pages inactive/ {gsub("\\.", "", $3); print $3}')
        echo "Total:         $((total_bytes / 1024 / 1024)) MB"
        echo "Free:          $((free_pages * page_size / 1024 / 1024)) MB"
        echo "Inactive:      $((inactive_pages * page_size / 1024 / 1024)) MB (can be reclaimed)"
    elif command -v free >/dev/null 2>&1; then
        free -h
    else
        # Minimal images may not ship procps; /proc/meminfo is always there
        grep -E '^(MemTotal|MemFree|MemAvailable|SwapTotal|SwapFree):' /proc/meminfo
    fi
}

report_disk() {
    section "Disk usage"
    if [ "$OS_TYPE" = "Darwin" ]; then
        # Skip the many read-only system volumes macOS mounts; user data lives on the Data volume
        df -h / /System/Volumes/Data
    else
        df -h
    fi
    section "Available disk space"
    # -P gives one line per filesystem, so column 4 is always "Available"
    echo "Available on /: $(df -Ph / | awk 'NR==2 {print $4}') (of $(df -Ph / | awk 'NR==2 {print $2}'))"
}

generate_report() {
    report_header
    report_student
    report_datetime
    report_user
    report_os
    report_cpu
    report_memory
    report_disk
    echo
}

# --- main ---

case "${1:-}" in
    -h|--help)
        usage
        exit 0
        ;;
    -*)
        usage >&2
        error "unknown option: $1"
        ;;
esac

LOG_FILE="${1:-$DEFAULT_LOG}"
OS_TYPE="$(uname -s)"

LOG_DIR="$(dirname "$LOG_FILE")"
[ -d "$LOG_DIR" ] || mkdir -p "$LOG_DIR" || error "cannot create log directory: $LOG_DIR"
touch "$LOG_FILE" 2>/dev/null || error "cannot write to log file: $LOG_FILE"

# Show the report on screen and append it to the log at the same time
generate_report | tee -a "$LOG_FILE"

echo "Report appended to: $LOG_FILE"
