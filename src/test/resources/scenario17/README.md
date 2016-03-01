Scenario 17 - post-2015 "1-2" partial consumption smoke test 2
==============================================================

See the description of the Scenario 16.

In this scenario we have:
- 2 products, id 814 is installed everywhere, except virtual hosts,
  id 815 is installed on systems on which no penalty for partially used 1-2
  subscriptions is happenning (even cpu socket count, even virtual guest count)
- 2 subscriptions, each for one product
- 2 physical systems (1 and 2 cpu sockets respectively)
- 3 virtual hosts, no products installed
- 1 + 2 + 1 virtual guests
- one of the virtual guests is invalid - it's reporting itself as virtual, but
  with no association to a virtual host. This system should be matched a as a
  physical system.

Result
------

There is a bunch of subscriptions that "almost match" the setup (one system of
each subscription is not matched to ease a detection of behavior after a code
change). We use pins to make the test repeatable.

