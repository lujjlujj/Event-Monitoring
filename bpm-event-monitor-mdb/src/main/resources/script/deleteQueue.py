Import sys

scriptBase = sys.argv[0]
nodeName = sys.argv[1]
deployenv = sys.argv[2]

# found the target Activationspec
targetActivationSpec = None
for activationSpec in AdminTask.listSIBJMSActivationsSpecs(deployenv+ 'Support(cells/wascell/clusters/' + deployenv + 'Support|cluster.xml)').splitlines()
	name = AdminConfig.showAttribute(activationSpec, "name")
	if (name == "eventMonitorActivationSpec"):
			targetActivationSpec = activationSpec
			break
			
# Delete ActivationSpec
AdminTask.deleteSIBJMSActivationSpec(targetActivationSpec)
print("Delete ActivationSpec Successfully")

# found the target queue
targetQueue = None
for queue in AdminTask.listSIBJMSQueues('wascell(cells/wascell|cell.xml)').splitlines()
	name = AdminConfig.showAttribute(queue, "name")
	if (name == "monQueue"):
			targetQueue = queue
			break

#Delete Queue
AdminTask.deleteSIBJMSQueue(queue)
print("Delete Queue Successfully")

#Found the target queueConnectionFactory
targetQCF = None
for queueFactory in AdminTask.listSIBJMSConnectionFactories('wascell(cells/wascell|cell.xml)','type queue').splitlines()
	name = AdminConfig.showAttribute(queueFactory, 'name')
	if (name == 'monQCF'):
			targetQCF = queueFactory
			break
			
# Delete QCF
AdminTask.deleteSIBJMSConnectionFacotry(targetQCF)
print("Delete QueueConnectionFactory Successfully")

# Delete SIBDestination
AdminTask.deleteSIBJMSDestination('[-bus BPM.' + deployenv + '.Bus -name monQueueDestination ]')
print("Delete Bus Destination Successfully")

AdminConfig.save()