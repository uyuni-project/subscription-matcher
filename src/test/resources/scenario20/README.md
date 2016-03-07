Scenario 20 - Match an Inherited Virtualization subs
====================================================

Contents:
- 2 subscriptions (1 for the base product (physical only policy), 1 for the
  inherit virtualization)
- 2 products - one is a base product, the other is an "addon" product
- 2 systems, one has both products installed, the other has only an addon
  product installed (todo.
  rework)

Result
------

Both subscription should match to the system with the base product. Nothing
must match to the other system even though there is enough addon subscriptions.

