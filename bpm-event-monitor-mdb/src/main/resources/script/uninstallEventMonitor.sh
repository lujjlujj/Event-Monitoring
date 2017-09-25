#!/usr/bin/env bash
## Uninstall the MDB package
$waspath/wsadmin.sh -lang jython -f $1/functionalscripts/uninstallMDB.py $1 $2 $3
retCode = $?
if [[$retCode -gt 0]]; then
    echo "Problem occurred while uninstalling mdb, may the application name does not exist."
    exit 1
fi

## Delete the qcf and queue
$waspath/wsadmin.sh -lang jython -f $1/functionalscripts/deleteQueue.py $1 $2 $3
retCode = $?
if [[$retCode -gt 0]]; then
    echo "Problem occurred while delete queues for event."
    exit 1
fi

##remove the bpm def configuration
$waspath/wsadmin.sh -lang jython -f $1/functionalscripts/removeEventConfiguration.py
retCode = $?
if [[$retCode -gt 0]]; then
    echo "Problem occurred while remove the bpm def configuration."
    exit 1
fi

$waspath/wsadmin.sh -lang jython -f $1/functionalscripts/ReloadDEF.py
retCode = $?
if [[$retCode -gt 0]]; then
    echo "Problem occurred while reload the bpm def."
    exit 1
fi