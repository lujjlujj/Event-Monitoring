import rt sys

scriptBase = sys.argv[0]
nodeName = sys.argv[1]
deployenv = sys.argv[2]

# Uninstall MDB
AdminApp.uninstall('event-monitor-ear')
print('Uninstall the mdb successfully')

AdminConfig.save()