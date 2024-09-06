# Contributing to Kapi

_Last Updated: September 4th, 2024_

Thank you so much for showing interest in contributing to Kapi!
Here are a set of instructions and guidelines for contributing code or documentation to the project.
This document will change over time, so make sure that your contributions still line up with the requirements here
before submitting a pull request.

> [!IMPORTANT]
> By contributing to Kapi, you agree that your contributions will be licensed under
> the [GNU Affero General Public License v3.0 (AGPLv3)](https://opensource.org/licenses/agpl-v3).
> This ensures that all contributions remain free and open-source.

## Getting Started

- Check the [Planned Features](https://kapimc.github.io/docs/planned-features) page,
  as well as the open [Issues](https://github.com/KapiMC/Kapi/issues)
  and [Pull Requests](https://github.com/KapiMC/Kapi/pulls).
- If you'd like to add a feature that isn't on the roadmap or doesn't have an open issue,
  **PLEASE create a feature request issue** discussing your intentions,
  so any feedback or important information can be given by the team first.  
  We don't want you to waste time developing a feature or making a change that can't/won't be added.

### Contribution Checklist

- I've read the [Planned Features](https://kapimc.github.io/docs/planned-features) page.
- I've checked the open [Issues](https://github.com/KapiMC/Kapi/issues)
  and [Pull Requests](https://github.com/KapiMC/Kapi/pulls).
- **I've created a new issue for my feature _before_ starting to work on it**,
  or have at least notified others in the relevant existing issue(s) of my intention to work on it.
- I've set up my development environment (for code contributions).
- I've read the [Code Guidelines](#code-guidelines) and/or [Documentation Guidelines](#documentation-guidelines)

## Creating a Development Environment

### Prerequisites

- [Java](https://www.java.com/en/download/manual.jsp)
- [JDK 17](https://adoptium.net/temurin/releases/?version=17) (can be downloaded automatically if using IntelliJ)
- [Gradle](https://gradle.org/install/) (can be downloaded automatically if using IntelliJ)
- [IntelliJ](https://www.jetbrains.com/idea/download) (optional, highly recommended)

### Clone the repo

#### Using IntelliJ

Go to file -> new -> Project from Version Control and click it.  
Make sure "git" is selected and set the URL field to: https://github.com/KapiMC/kapi
Click "Clone".

#### Using Git

Clone the repo by running:

```sh
$ git clone https://github.com/KapiMC/docs.git
```

### Compiling and Testing using IntelliJ

- Make sure IntelliJ has detected the Gradle project.
- To compile, run the `gradle build` task, it's recommended to set this as your run configuration.
- To test, run the `gradle test` task, it's recommended to set this as your run configuration.

If Gradle successfully compiled the project, the `jar` should be available at `Kapi/build/libs/`.

### Manually Compiling and Testing (Outside an IDE)

- **Windows**

    - To compile, run `gradlew.bat build` in command prompt or PowerShell.
    - To test, run `gradlew.bat test` in command prompt or PowerShell.

- **Linux/macOS**

    - To compile, run `gradlew build` in your shell.
    - To test, run `gradlew test` in your shell.

If Gradle successfully compiled the project, the `jar` should be available at `Kapi/build/libs/`.

## Code Guidelines

### Style

Most of the style guidelines can be checked, fixed, and enforced via IntelliJ's Code Style Checker.  
Go to Settings -> Editor -> Code Style, make sure "Scheme" is set to "Project".  
Go to Settings -> Editor -> Inspections, make sure "Profile" is set to "Project Default".

- Do your best to write clear, concise, and modular code.
- Try to keep a maximum column width of no more than **100** characters, max is 120.
- Code comments should be used to help describe sections of code that don't speak for themselves.
- Add the `@Kapi` annotation for anything that is intended to be a part of the public API.
- Use [Javadoc](https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html) to document any classes,
  methods, and enums you add.
- Javadoc `@param`, `@return` and `@throws` should start with a lowercase letter,
  Javadoc descriptions should be English sentences starting with a capital letter.
- If you're modifying an existing method that does _not_ have a Javadoc,
  you don't _have_ to add a Javadoc to it... but it would be pretty cool if you did ;)
- Names (identifiers) should not contain multiple uppercase letters in a row (except for constants which should be
  SCREAMING_SNAKE_CASE), for example, `Uuid` not `UUID`, `encryptRsaKey` not `encrpyRSAKey`.
- If something wasn't mentioned above and not enforced by IntelliJ, stick to Java's conventions and best practices.

### Code Quality

- Avoid nested if statements as much as possible.
- Use `Log` instead of `System.out.println()` statements.
- Prefer using `Option` and `Result` over `null` and exceptions.
- Limit visibility of classes, methods, fields, and variables to the minimum necessary.
- Prefer composition over inheritance, only use inheritance when needed, and prefer interfaces over abstract classes.
- Use design patterns appropriately, don't overuse or misuse them.
- Write tests for your code, or alternatively a Minecraft plugin using the code, to verify it works in-game.

#### Git/GitHub Specifics

- Use clear and concise commit messages. If your commit does too much, either consider breaking it up into smaller
  commits or providing extra detail in the commit description.
- Follow [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/), use past tense where it makes sense.
- Pull Requests should have an adequate title and description which clearly outline your intentions and
  changes/additions. Feel free to provide screenshots, GIFs, or videos, especially for visual changes.

## Documentation Guidelines

Documentation contributions are contributions to [Kapi's Docs Website](https://kapimc.github.io/docs)

- Use [kebab-case](https://developer.mozilla.org/en-US/docs/Glossary/Kebab_case) for file names
- Follow the existing directory structure
- Provide alt text for all embedded media
- Use [Title Case](https://apastyle.apa.org/style-grammar-guidelines/capitalization/title-case) for Markdown headers
