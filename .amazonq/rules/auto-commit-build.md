# Auto-Commit After Successful Build Rule

## MANDATORY: Commit changes after every successful build

**After every successful `make build` or compilation:**

```bash
# Stage all changes
git add .

# Commit with build timestamp
git commit -m "Build successful - $(date '+%Y-%m-%d %H:%M:%S')"

# Show commit details
git show --stat HEAD
```

## Required Workflow

**1. Make changes to code**
**2. Build the project:**
```bash
make build
```

**3. If build succeeds, immediately commit:**
```bash
git add .
git commit -m "Build successful - $(date '+%Y-%m-%d %H:%M:%S')"
git show --stat HEAD
```

**4. For feature commits, use descriptive messages:**
```bash
git add .
git commit -m "feat: [description] - $(date '+%Y-%m-%d %H:%M:%S')"
git show --stat HEAD
```

## Commit Message Formats

**Successful builds:**
- `"Build successful - YYYY-MM-DD HH:MM:SS"`

**Feature additions:**
- `"feat: [description] - YYYY-MM-DD HH:MM:SS"`

**Bug fixes:**
- `"fix: [description] - YYYY-MM-DD HH:MM:SS"`

**Refactoring:**
- `"refactor: [description] - YYYY-MM-DD HH:MM:SS"`

## Enforcement
- ❌ **NO successful builds without commits**
- ✅ **MUST commit immediately after successful compilation**
- ✅ **MUST show commit details with `git show --stat HEAD`**
- ✅ **MUST include timestamp in commit message**

## Integration with Development Workflow
```bash
# Standard development cycle
make build && git add . && git commit -m "Build successful - $(date '+%Y-%m-%d %H:%M:%S')" && git show --stat HEAD
```

## Purpose
- Track all working code states
- Maintain build history
- Enable easy rollback to last working version
- Document development progress with timestamps
