# Auto-Commit After Successful Build Rule

## MANDATORY: Commit changes after every successful build

**After every successful `./control.sh build` or compilation:**

```bash
# Stage all changes
git add .

# Commit with JIRA ticket and timestamp
git commit -m "JIRA-XXX: Build successful - $(date '+%Y-%m-%d %H:%M:%S')"

# Show commit details
git show --stat HEAD
```

## Required Workflow

**1. Make changes to code**
**2. Build the project:**
```bash
./control.sh build
```

**3. If build succeeds, immediately commit:**
```bash
git add .
git commit -m "JIRA-XXX: Build successful - $(date '+%Y-%m-%d %H:%M:%S')"
git show --stat HEAD
```

**4. For feature commits, use descriptive messages:**
```bash
git add .
git commit -m "JIRA-XXX: feat: [description] - $(date '+%Y-%m-%d %H:%M:%S')"
git show --stat HEAD
```

## Commit Message Formats

**All commits MUST start with JIRA ticket:**

**Successful builds:**
- `"JIRA-XXX: Build successful - YYYY-MM-DD HH:MM:SS"`

**Feature additions:**
- `"JIRA-XXX: feat: [description] - YYYY-MM-DD HH:MM:SS"`

**Bug fixes:**
- `"JIRA-XXX: fix: [description] - YYYY-MM-DD HH:MM:SS"`

**Refactoring:**
- `"JIRA-XXX: refactor: [description] - YYYY-MM-DD HH:MM:SS"`

## Enforcement
- ❌ **NO commits without JIRA ticket prefix**
- ❌ **NO successful builds without commits**
- ✅ **MUST start all commits with JIRA-XXX:**
- ✅ **MUST commit immediately after successful compilation**
- ✅ **MUST show commit details with `git show --stat HEAD`**
- ✅ **MUST include timestamp in commit message**

## Integration with Development Workflow
```bash
# Standard development cycle
./control.sh build && git add . && git commit -m "JIRA-XXX: Build successful - $(date '+%Y-%m-%d %H:%M:%S')" && git show --stat HEAD
```

## Purpose
- Track all working code states with JIRA traceability
- Maintain build history linked to tickets
- Enable easy rollback to last working version
- Document development progress with timestamps and ticket references
