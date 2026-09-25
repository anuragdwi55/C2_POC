# Command: Review code

Prompt to use in Cursor/Copilot:

```
Review the diff against spec/api-contract.md and spec/state-machine.md.
List: spec violations, missing validation, state machine leaks, security issues, test gaps.
Do not suggest large refactors unless required for correctness.
```

Checklist:

- [ ] Status transitions only in service layer
- [ ] DTO validation matches data-model.md
- [ ] Error envelope matches api-contract.md
- [ ] No hardcoded credentials
