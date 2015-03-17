#!/usr/bin/env python

# https://wiki.innerweb.novell.com/index.php/Subscription_Management
# https://wiki.innerweb.novell.com/index.php/Subscription_Management_FAQ

from pyke import knowledge_engine, goal

# compile and load .krb files in the same directory
engine = knowledge_engine.engine(__file__)

engine.activate('preparation_rules')

engine.activate('matching_rules')
