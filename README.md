# Netbeans perforce Plugin

This netbeans plugin adds a Perforce submenu and to your Teams menu along with the following 4 commands:

* Check Out - checks out the current file
* Diff - Diffs the current file against the head revision
* History - Opens a history window of the current file
* Time Lapse - Opens a time lapse view of the current file

This plugin expects:
* p4  and p4vc to be accessible. Locations can be configured via Options -> Team -> Perforce
* p4port and p4user variables to be set via p4

It will automatically look up the correct workspace for the current file. Tested on Windows, Linux and OSX.

If any additional functionality (such as cross platform support) is required, please create an issue so they can be added.
