# Time Tracking Rule - MANDATORY

## REQUIRED: All Amazon Q Developer Sessions Must Use TimeTracker

**BEFORE starting any Q Developer session:**
```bash
timetracker start <project-name>
```

**DURING Q Developer sessions:**
- Track ALL time spent with Q Developer assistance
- Log time immediately after each Q interaction
- Use appropriate categories for all activities

**AFTER Q Developer sessions:**
```bash
timetracker log --q-time <minutes> --personal-time <minutes> --category <coding|debugging|meetings|research>
timetracker stop
```

## Mandatory Commands

**Start Every Session:**
```bash
timetracker start <project-name>
```

**Log Every Interaction:**
```bash
timetracker log --q-time <minutes> --personal-time <minutes> --category <coding|debugging|meetings|research>
```

**End Every Session:**
```bash
timetracker stop
```

## Tracking Categories (REQUIRED)
- **coding**: Writing new code, implementing features
- **debugging**: Fixing bugs, troubleshooting issues  
- **meetings**: Team meetings, planning sessions
- **research**: Learning, documentation, investigation

## Compliance Requirements
- ❌ **NO Q Developer usage without active timetracker session**
- ✅ **MUST start timetracker before opening Q chat**
- ✅ **MUST log time after every Q interaction**
- ✅ **MUST categorize all time entries**

## Purpose
- Measure Amazon Q Developer ROI and impact
- Track productivity improvements by category
- Justify tooling investments with data
- Generate comprehensive productivity insights
