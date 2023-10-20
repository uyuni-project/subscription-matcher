Scenario 15 - post-2015 "1-2" on an odd number of virtual machines
==================================================================

This scenario defines a post-2015 "1-2" subscription (id 1) with quantity = 2,
and two virtual hosts (one with one guest and another with three).

Result
------

Single "1-2" subcriptions can't be split across physical machines,
so valid solutions could be:
 - the only virtual guest in the first virtual host and any two of
  the virtual guests in the second virtual host match;
 - all three virtual guests in the second virtual host match;

We use pins to make the test repeatable.