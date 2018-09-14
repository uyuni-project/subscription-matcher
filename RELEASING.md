# How to release

## Common steps

1. Edit `pom.xml` to increase the version
2. Commit the change and create a PR
3. Get the PR accepted
4. [Generate a release](https://github.com/openSUSE/subscription-matcher/releases/new) Tag and Release Title will be vX.YY where X.YY is the version you have at pom.xml. Description will be the list of changes you will add at the changelog for the RPM.
5. Look if there are new dependencies since last release, at the `pom.xml` file.

## If there are no new Java dependencies

6. Proceed to your working copy of systemsmanagement:Uyuni:Master and the package subscription matcher
7. Run `SMVER=X.YY; wget https://github.com/openSUSE/subscription-matcher/archive/v${SMVER}.tar.gz && tar -xzf v${SMVER}.tar.gz && mv subscription-matcher-${SMVER} subscription-matcher && tar -czf subscription-matcher.tar.gz subscription-matcher && rm -rf subscription-matcher v${SMVER}.tar.gz` (replace X.YY with the version you have at pom.xml)
8. Change the version at subscription-matcher.spec
9. Run `osc vc` and add changes
10. Run `osc build` to check that the package is building
11. Commit the changes with `osc ci`
12. Copypac the package to all the Devel:Galaxy:Manager:X.Y projects with maintenance, with `iosc copypac openSUSE.org:systemsmanagement:Uyuni:Master subscription-matcher Devel:Galaxy:Manager:X.Y` or `osc -A https://api.suse.de copypac openSUSE.org:systemsmanagement:Uyuni:Master subscription-matcher Devel:Galaxy:Manager:X.Y`

## If there are new Java dependencies

6. Use [tetra](https://github.com/moio/tetra) to generate the main package and the kit package. Remember to adjust the SPEC file and changelog
7. Copy the main package to your working copy of Devel:Galaxy:Manager:Head and the kit package to your working copy of Devel:Galaxy:Manager:Head:Kit
8. Commit both packages with `iosc ci` if you have the alias or `osc -A https://api.suse.de` otherwise
9. Copypac the main package to systemsmanagement:Uyuni:Master with `iosc copypac -t https://api.opensuse.org/ Devel:Galaxy:Manager:Head subscription-matcher systemsmanagement:Uyuni:Master` or `osc -A https://api.suse.de copypac -t https://api.opensuse.org/ Devel:Galaxy:Manager:Head subscription-matcher systemsmanagement:Uyuni:Master`
10. Copypac the main package and the kit to all other Devel:Galaxy:Manager:X.Y projects with maintenance

During submission time for SUSE Manager remember you will need to submit both the main and the kit packages.
