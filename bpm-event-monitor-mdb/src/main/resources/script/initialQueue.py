import sys

scriptBase = sys.argv[0]
nodeName = sys.argv[1]
deployenv = sys.argv[2]
authorization_alias = sys.argv[3]

# Add Destination
AdminTask.createSIBDestination('[-bus BPM.' + deployenv + '.Bus -name monQueueDestination' + 
' -type Queue -reliability ASSURED_PERSISTENT -description -cluster ' + deployenv + '.Messaging ]')
print("Created destination on bus")

#Add QCF and Queue
AdminTask.createSIBDesinationFactory('wascell(cells/wascell|cell.xml)', '[-type queue -name monQCF -jndiName jms/monQCF ' + 
'-description -category -busName BPM.' + deployenv + 'Bus -nonPersistentMapping ExpressNonPersistent ' + 
'-readAhead Default -tempQueueNamePrefix -target -targetType BusMember -targetSignificance Preferred ' + 
'-targetTransportChain -providerEndPoints -connectionProximity Bus -authDataAlias -containerAuthAlias ' + authorization_alias +
' -mappingAlias -shareDataSourceWithCMP false -logMissingTransactionContext false -manageCachedHandles false -xaRecoveryAuthAlias ' +
authorization_alias + 'persistentMapping ReliablePersistent -consumerDoesNotModifyPayloadAfterGet false '+ 
'-produceDoesNotModifyPayloadAfterSet false]')
print('Added QCF')

AdminTask.createSIBJMSQueue('wascell(cells/wascell|cell.xml)', '[-name monQueue -jndiName jms/monQueue -description ' +
'- deliveryMode Application -readahead AsConnection -busName BPM.' + deployenv + '.Bus -queueName monQueueDestination '+
' -scopeToLocalQP false -producerBind false -producerPreferlocal true -gatherMessages false]')
print("Add Queue")

# Add ActivationSpec
AdminTask.createSIBJMSActivationSpec(deployenv + '.Support(cells/wascell/clusters/' + deployenv + '.Support|cluster.xml)', 
'[-name eventMonitorActivationSpec -jndiName esi/eventMonitorActivationSpec -destinationJndiName jms/monQueue ' + 
' -description -busName BPM.' + deployenv + '.Bus -clientId -durableSubscriptionHome ' + deployenv + '.Messaging.000-BPM.' + deployenv+
'.Bus -destinationType javax.jms.Queue -messageSelector -acknowledgeMode Auto-acknowledge -subscriptionName -maxBatchSize 1' + 
' -maxConcurrency 10 -subscriptionDurability Nondurable -sharedurable -shareDurableSubscriptions inCluster -authenticationAlias ' + authorization_alias +
' -readAhead Defaul -target -targetType BusMember -targetSignficance Preferred -targetTransportChain -providerEndPoints' + 
' -shareDataSourceWithCMP false -consumerDoesNotModifyPayloadAfterGet false -forwarderDoesNotModifyPayloadAfterSet false' +
' -alwaysActivateAllMDBs false -retryInterval 30 -autoStopSequentialMessageFailure 0 -failingMessageDelay 0]')
print('Added event monitor ActivationSpec')

AdminConfig.save()