Scenario 47 - Adding part numbers for promotion subscriptions
==============================================================

This scenario refres to fix bsc#1256392 (https://bugzilla.suse.com/show_bug.cgi?id=1256392)

This bug points out that 3 promotion subscriptions are shown as "Unsupported part number detected", since the part numbers are actually not defined.

The subscriptions are: 

- P-874-008272-C
  SUSE Multi-Linux Support Enterprise, x86-64, 1-2 Sockets or 1-2 Virtual Machines, Priority Subscription, 3 Year, Promotion

- P-874-008273-C
  SUSE Multi-Linux Support Enterprise, x86-64, 1-2 Sockets with Unlimited Virtual Machines, Priority Subscription, 3 Year, Promotion

- P-874-008285-C
  SUSE Multi-Linux Support Professional, x86-64, 1-99 subscriptions, 1-2 Sockets or 1-2 Virtual Machines, Priority Subscription, 1 Year, Promotion

The scenario files were originally taken from the user support config, then modified to get the right setup
  
Result
------

The 3 formerly missing subscriptions now are shown as correctly matched in the output
