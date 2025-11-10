# Git Flow Requirements - MANDATORY

## Git Flow Setup
This project uses Git Flow branching model. All developers MUST follow these conventions.

### Initial Setup
```bash
git flow init -d
```

### Branch Structure
- **main**: Production-ready code
- **develop**: Integration branch for features
- **feature/***: New features
- **release/***: Release preparation
- **hotfix/***: Production fixes

### Required Workflow

#### Feature Development
```bash
# Start new feature
git flow feature start feature-name

# Work on feature
git add .
git commit -m "feat: description"

# Finish feature (merges to develop)
git flow feature finish feature-name
```

#### Release Process
```bash
# Start release
git flow release start 1.0.0

# Prepare release (version bumps, docs)
git add .
git commit -m "release: prepare 1.0.0"

# Finish release (merges to main and develop, creates tag)
git flow release finish 1.0.0
```

#### Hotfix Process
```bash
# Start hotfix
git flow hotfix start 1.0.1

# Fix issue
git add .
git commit -m "fix: critical issue"

# Finish hotfix (merges to main and develop, creates tag)
git flow hotfix finish 1.0.1
```

### Commit Message Conventions
- **feat**: New features
- **fix**: Bug fixes
- **docs**: Documentation changes
- **style**: Code style changes
- **refactor**: Code refactoring
- **test**: Test additions/changes
- **chore**: Build/tooling changes

### Enforcement Rules
- ❌ **NO direct commits to main**
- ❌ **NO direct commits to develop** (except merges)
- ✅ **MUST use feature branches for all changes**
- ✅ **MUST use semantic commit messages**
- ✅ **MUST finish features properly**

### Integration with JFunctions
Use JFunctions Git Flow commands:
```bash
jf git-flow-feature-start feature-name
jf git-flow-feature-finish feature-name
jf git-flow-release-start 1.0.0
jf git-flow-release-finish 1.0.0
```
