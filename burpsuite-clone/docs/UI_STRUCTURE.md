# Burp Suite v2026.11.2 UI Structure (from decompiled source)

## Dashboard (Zea5 extends Zech/JPanel)
- **Layout**: BorderLayout
- **Center**: Zr2e (task list area) — contains Zeb3 task entry cards
- **North**: Zeb9 (only shown in Community Edition, "Pro only" banner)
- **Empty state**: Zq1e builder with message "Real-time information on your scans and live tasks is displayed here..."
  - "New scan" button → triggers Zu4k.ZP() (opens scan dialog)
  - "New live task" button → triggers Zu4k.ZB() (opens live task dialog)

### Dashboard Button Panel (Zebx extends Zech)
- **Layout**: GridBagLayout, columnWidths = {0, 10, 0} (10px gap between buttons)
- "New scan" button (Za9w, name="newScanButton") at gridx=0
  - Pro: ActionListener → zu4k.ZP() (opens scan launcher)
  - Community: MouseListener → shows "Only available in Burp Suite Pro" tooltip, disabled
- "New live task" button (Za99, name="newLiveTaskButton") at gridx=2
  - Always enabled, ActionListener → zu4k.ZB() (opens live task launcher)

### Dashboard Controls
- "dashboardPauseAllTasksButton" — pauses all running tasks
- "dashboardResumeAllTasksButton" — resumes all paused tasks
- "dashboardEventLogTable" — event log table at bottom
- "dashboardEventLogEmptyState" — empty state message for event log
- "dashboardTaskProgressBar" — progress bar for current task
- "dashboardTasksFilterLine" — filter/search line for task list
- "dashboardTasksPausedWarningBar" — warning bar when tasks are paused

### Task List Entry (Ze5y extends Zech)
- Per-task card with:
  - "taskListEntryResumeButton" — resume paused task
  - "taskListEntryShowDetailsButton" — show task details
  - "taskListEntryOptionsMenu" — context menu
  - "taskListEntryDeleteMenuEntry" — delete task
  - "taskListEntryRenameMenuEntry" — rename task
  - "taskListEntryScanAgainMenuEntry" — re-run scan
  - "taskListEntrySettingsMenuEntry" — task settings
  - "taskListEntryActivity" — activity indicator

## Scan Launcher Dialog (Z_sw extends Z_s4/JDialog)
- **Tab navigation**: Zr20 (left nav panel + center content area)
- **Tabs** (Zrgr enum, in order):
  1. SCAN_TYPE — scan type selection
  2. SCAN_DETAILS (Zrz7) — URL list, scope, protocol, port, isolated scan
  3. SELECTED_ITEMS (Zrut) — "Add to task" / "Create new task" radio + task selector
  4. CONFIGURATION (Zru2) — scan configuration (crawl + audit settings)
  5. APP_LOGIN (Zru_) — application login (credentials, recorded logins)
  6. RESOURCE_POOLS (Zruh) — threads, delay, scan speed, retries, throttle
  7. LIVE_TASK_DETAILS (Zrun) — live task specific options
  8. API_DEFINITION / API_DETAILS — API scanning (when applicable)

### Scan Details Panel (Zrz7)
- **Layout**: GridBagLayout, columnWidths={0,5,0,5,0}, rowHeights={0,5,0,5,0,5,0}
- "Advanced scan parameters" section header (Zr0m)
- "Use this setting to configure whether Burp should isolate the scan..." (Zahm)
- "Run isolated scan" checkbox (Zuc9, name not set in this class)
- Contains Zrz4 (main content area) at gridx=0, gridy=2, gridwidth=3

### Scan Configuration Panel (Zru2)
- Crawl configuration (max depth, max links, etc.)
- Audit configuration (insertion points, checks)
- Built-in configurations selector
- Edit/New/Delete configuration buttons

### Application Login Panel (Zru_)
- Login type selector (none, use recorded, use credentials)
- Username/password fields
- Session handling options
- "Recorded logins" management

### Resource Pool Panel (Zruh)
- Thread count slider/input
- Delay between requests
- Scan speed selector (fast/normal/slow)
- Retry count
- Throttle settings

## Key Component Names
| Name | Type | Location |
|------|------|----------|
| newScanButton | Za9w (button) | Dashboard button panel, gridx=0 |
| newLiveTaskButton | Za99 (button) | Dashboard button panel, gridx=2 |
| scanLauncherScanButton | button | Scan dialog footer |
| scanLauncherCancelButton | button | Scan dialog footer |
| dashboardPauseAllTasksButton | button | Dashboard toolbar |
| dashboardResumeAllTasksButton | button | Dashboard toolbar |
| dashboardEventLogTable | JTable | Dashboard bottom panel |
| taskListEntryResumeButton | button | Per-task card |
| taskListEntryShowDetailsButton | button | Per-task card |
| taskListEntryOptionsMenu | JPopupMenu | Per-task card |
| burpBottomDockEventLogPanel | panel | Bottom dock |
| burpBottomDockEventLogTable | JTable | Bottom dock |

## Dialog Structure
- Scan dialog footer: Cancel + "Scan" buttons
- Left navigation sidebar (Zf/ZH) with tab buttons
- Center content area (Zr2e) showing selected tab
- Error message label (scanLauncherErrorMessageLabel) at bottom
