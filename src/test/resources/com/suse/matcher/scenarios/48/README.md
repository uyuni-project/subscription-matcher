Scenario 48 - Adding part number 874-008422
==============================================================

This scenario tests the fix for bsc#1259243 (https://bugzilla.suse.com/show_bug.cgi?id=1259243)

There is another new SKU showed up missing in subscription matching for one of our customers:

Unsupported part number detected - 874-008422
SUSE Linux Enterprise Server, x86-64, 1-2 Sockets, Standard Subscription, 3 Year 

The scenario files were originally taken from the user support config,
and then simplified to check that the actual match works
  
Result
------

Part number 874-008422 is now correctly detected
