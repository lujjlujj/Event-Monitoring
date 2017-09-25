#!/usr/bin/env bash
## Initial the qcf and queue
$waspath/wsadmin.sh -lang jython -f $1/functionalscripts/initalQueue.py $1 $2 $3 $4
retCode = $?
if [[$retCode -gt 0]]; then
    echo "Problem occurred while adding queues for event, may the same queue names existing."
    exit 1
fi

##install the MDB package
$waspath/wsadmin.sh -lang jython -f $1/functionalscripts/installMDB.py $1 $2 $3
retCode = $?
if [[$retCode -gt 0]]; then
    echo "Problem occurred while installing mdb, may the same application name existing."
    exit 1
fi

##enable the bpm def feature
$waspath/wsadmin.sh -lang jython -f $1/functionalscripts/ConfigureEventsToJMS.py $4
retCode = $?
if [[$retCode -gt 0]]; then
    echo "Problem occurred while enable the bpm def feature."
    exit 1
fi

$waspath/wsadmin.sh -lang jython -f $1/functionalscripts/ReloadDEF.py
retCode = $?
if [[$retCode -gt 0]]; then
    echo "Problem occurred while reload the bpm def."
    exit 1
fi