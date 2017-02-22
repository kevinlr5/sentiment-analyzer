# conjugates

A server that can analyze the Russell Conjugation of inputs

- Russell Conjugation: https://en.wikipedia.org/wiki/Emotive_conjugation
- Repo: https://github.com/kevinlr5/conjugates

## Developer Setup

### Eclipse

1. Install the latest version of eclipse
2. Import and use the eclipse style settings in ide-resources/eclipse-java-google-style.xml
3. Run `./gradlew clean eclipse` and import the projects

## Builds

Conjugates uses gradle as its build system. The gradle wrapper is committed to the repo. Additionally, the build configuration uses a multi-project build.

- Unbreakable test build: `./gradlew clean build asciidoctor`
- Run server: `./gradlew clean bootRun`
- Build runnable server jar: `./gradlew clean build`
- Build server html documentation: `./gradlew clean asciidoctor`

## CI

Every PR to develop kicks off a CircleCI build that runs the unbreakable build

## Versioning and Releases

### Branching and Releasing
This repository uses the git flow branching model: http://nvie.com/posts/a-successful-git-branching-model/.

- To release, merge develop into master and tag the merge commit in master. Then push tags.
- If necessary, release branches can be created for testing and bugfixing before the merge to master
- After the merge to master, master must be back-merged into develop
- Command for merging to master: `git merge develop --no-ff`
- Hotfixes are branched from master and then merged back to master. After hotfixing, master is backmerged to develop.

### Versioning

- Versions conform to semantic versioning: http://semver.org/
