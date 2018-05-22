# Jenkins-Library

This is a repository that contains a bunch of Groovy scripts that enabled new
methods used by Cilium Jenkinsfiles

List of exported methods:

- `BuildIfLabel`: Build a new async job based on a Pull Request Label.
- `ispr`: Return true if the job is triggered by a PR.
- `setIfPr`: returns the given argument if it's a PR or not. Useful to set ENV
  variables. `Option=setIfPR("true", "false")`
- `setIfLabel`: returns the given argument if the PR has the given label. Useful to set ENV
  variables when a label is present in the PR.
- `Status`: To set commit status in Github Pull Request.

