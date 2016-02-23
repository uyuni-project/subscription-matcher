Scenario 15 - post-2015 "1-2" on a physical machine with odd socket count
=========================================================================

This scenario defines a post-2015 "1-2" subscription (id 1) with quantity = 2
and two physical machines (one with 1 cpu socket, the other with 3 cpu
sockets).

Result
------

Single "1-2" subcription can't be split to more machines, that's why the number
of consumed cents are always rounded to 100 * N (a single socket machine
consumes one 1-2 subcription, 3-socket machine consumes 2 1-2 subscriptions
etc.).

The result: the subscription can't match both the systems in this test.

