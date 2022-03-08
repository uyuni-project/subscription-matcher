# How to release

## Common steps

1. Edit `pom.xml` to increase the version
2. Commit the change and create a PR
3. Get the PR accepted
4. [Generate a release](https://github.com/openSUSE/subscription-matcher/releases/new) Tag and Release Title will be vX.YY where X.YY is the version you have at pom.xml. Description will be the list of changes you will add at the changelog for the RPM.
5. Look if there are new dependencies since last release, at the `pom.xml` file.

## If there are no new Java dependencies

6. Branch `subscription-matcher` from `systemsmanagement:Uyuni:Master` checkout the package, and go to the checkout.
7. Run `SMVER=X.YY; wget https://github.com/openSUSE/subscription-matcher/archive/v${SMVER}.tar.gz && tar -xzf v${SMVER}.tar.gz && mv subscription-matcher-${SMVER} subscription-matcher && tar -czf subscription-matcher.tar.gz subscription-matcher && rm -rf subscription-matcher v${SMVER}.tar.gz` (replace X.YY with the version you have at pom.xml)
8. Change the version at subscription-matcher.spec
9. Run `osc vc` and add changes
10. Run `osc build` to check that the package is building
11. Commit the changes with `osc ci`
12. Verify that the package is still building in your branch
13. Run `osc sr` to prepare a submit request
14. Repeat for the 6-13 for the required `Devel:Galaxy:Manager:[Head|X.Y]` codestreams.
15. Ping the release engineer.

## If there are new Java dependencies

1. Use [tetra](https://github.com/moio/tetra) to generate the main package and the kit package. Remember to adjust the SPEC

For Head and Uyuni:
2. Branch `subscription-macher-kit` from `Devel:Galaxy:Manager:Head:Kit`, checkout and go to the checkout.
3. Copy the spec, and tarball to the checkout of `subscription-macher-kit`
5. Adjust the changelog with `osc vc`
6. Commit the changes with `osc ci`
7. Run `osc sr` to prepare a submit request
8. Verify that the package is still building in your branch
9. Run `osc sr` to prepare a submit request, and ping the Release Engineer
10. Branch `subscription-macher` from `Devel:Galaxy:Manager:Head`, checkout and go to the checkout.
11. Copy the spec, and tarball to the checkout of `subscription-macher`.
12. Adjust the changelog with `osc vc`
13. Commit the changes with `osc ci`
14. Run `osc sr` to prepare a submit request
15. Repeaat 10-14 for Uyuni.

For maintained versions of SUSE Manager (X.Y): 
1. Branch `subscription-macher-kit` from `Devel:Galaxy:Manager:Head:X.Y`, checkout and go to the checkout.
2. Copy the spec, and tarball to the checkout of `subscription-macher-kit`
3. Adjust the changelog with `osc vc`
4. Commit the changes with `osc ci`
5. Run `osc sr` to prepare a submit request
6. Branch `subscription-macher` from `Devel:Galaxy:Manager:Head:X.Y`, checkout and go to the checkout.
7. Copy the spec, and tarball to the checkout of `subscription-macher`
8. Adjust the changelog with `osc vc`
9. Commit the changes with `osc ci`
10. Run `osc sr` to prepare a submit request
11. Repeat 1-10 for the rest of affected SUSE Manager versions.

