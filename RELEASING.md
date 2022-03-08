# How to release

## Common steps

1. Edit `pom.xml` to increase the version
2. Commit the change and create a PR
3. Get the PR accepted
4. [Generate a release](https://github.com/openSUSE/subscription-matcher/releases/new) Tag and Release Title will be vX.YY where X.YY is the version you have at pom.xml. Description will be the list of changes you will add at the changelog for the RPM.
5. Look if there are new dependencies since last release, at the `pom.xml` file.

## If there are no new Java dependencies

1. Branch `subscription-matcher` from `systemsmanagement:Uyuni:Master` checkout the package, and go to the checkout.
2. Run `SMVER=X.YY; wget https://github.com/openSUSE/subscription-matcher/archive/v${SMVER}.tar.gz && tar -xzf v${SMVER}.tar.gz && mv subscription-matcher-${SMVER} subscription-matcher && tar -czf subscription-matcher.tar.gz subscription-matcher && rm -rf subscription-matcher v${SMVER}.tar.gz` (replace X.YY with the version you have at pom.xml)
3. Change the version at subscription-matcher.spec
4. Run `osc vc` and add changes
5. Run `osc build` to check that the package is building
6. Commit the changes with `osc ci`
7. Verify that the package is still building in your branch
8. Run `osc sr` to prepare a submit request
9. Repeat for the 6-13 for the required `Devel:Galaxy:Manager:[Head|X.Y]` codestreams.
10. Ping the release engineer.

NOTE: `osc` only works by default for the public OBS instance (build.opensuse.org). Adjust your command to work with the internal one for `Devel:Galaxy:Manager:*` codestreams.

## If there are new Java dependencies

1. Use [tetra](https://github.com/moio/tetra) to generate the main package and the kit package. Remember to adjust the SPEC

For Head and Uyuni:
1. Branch `subscription-macher-kit` from `Devel:Galaxy:Manager:Head:Kit`, checkout and go to the checkout.
2. Copy the spec, and tarball to the checkout of `subscription-macher-kit`
3. Adjust the changelog with `osc vc`
4. Commit the changes with `osc ci`
5. Run `osc sr` to prepare a submit request
6. Verify that the package is still building in your branch
7. Run `osc sr` to prepare a submit request, and ping the Release Engineer
8. Branch `subscription-macher` from `Devel:Galaxy:Manager:Head`, checkout and go to the checkout.
9. Copy the spec, and tarball to the checkout of `subscription-macher`.
10. Adjust the changelog with `osc vc`
11. Commit the changes with `osc ci`
12. Run `osc sr` to prepare a submit request
13. Repeat 10-14 for Uyuni.

For maintained versions of SUSE Manager (X.Y): 
1. Branch `subscription-macher-kit` from `Devel:Galaxy:Manager:Head:X.Y`, checkout and go to the checkout. If the package doesn't exist, it's because it was never changed since X.Y GA. Branch it from `SUSE:SLE-15-SP<N>:ZUpdate:Products:Manager<XY>`, and when you do the submit request later, make sure you do it against `Devel:Galaxy:Manager:Head:X.Y`
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

NOTE: `osc` only works by default for the public OBS instance (build.opensuse.org). Adjust your command to work with the internal one for `Devel:Galaxy:Manager:*` codestreams.
