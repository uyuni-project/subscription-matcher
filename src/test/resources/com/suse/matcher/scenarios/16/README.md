Scenario 16 - post-2015 "1-2" partial consumption smoke test
============================================================

This scenario tests the property of post-2015 "1-2" subscriptions that states
the whole subscription is consumed even if just one half of it is matched (this
can happen on odd cpu sockets on a physical machine or odd number of virtual
machines on a host).

In this scenatio we have:
- 2 products, id 814 is installed on each machine (except virtual hosts),
  id 815 is installed only on selected machines
- 2 1-2 subscriptions, each for one product
- 4 physical machines, various cpu socket counts
- 2 virtual hosts
- 3 + 3 virtual guests

Result
------

There is a bunch of subscriptions that "almost match" the setup (one system of
each subscription is not matched to ease a detection of behavior after a code
change). We use pins to make the test repeatable.

