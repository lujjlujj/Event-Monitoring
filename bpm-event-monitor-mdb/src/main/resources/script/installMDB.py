Import sys
Import time

scriptBase = sys.argv[0]
nodeName = sys.argv[1]
deployenv = sys.argv[2]

# Install MDB
AdminApp.install(scriptBase + '/event-monitor.ear', 
'[ -nopreCompileJSPs -distributeApp -nouseMetaDataFromBinary -nodeployejb -appname event-monitor-ear '+
' -createMBeansForResources -noreloadEnabled -nodeployws -validateinstall warn -noprocessEmbeddedConfig' +)
' -filepermission .*\.ddl=775#.*\.so=755#.*\.sl=755 -noallowDispatchRemoteInclude' + 
' -noallowServiceRemoteInclude asyncRequestDispatchType DISABLED -nouseAutoLink ' + 
' -noenbaleClientModule -clientMode isolated -novalidateSchema -MapModulesToServers' +
' [[ event-monitor-mdb-1.0.0.jar event-monitor-mdb-1.0.0.jar, META-INF/ejb-jar.xml WebSphere:cell=wascell,cluster='+deployenv+'.Support ]]' +
' -BindJndiForEJBMessageBinding [[ event-monitor-mdb-1.0.0.jar MonitorEventMessageDrivenBean ' + 
'event-monitor-mdb-1.0.0.jar, META-INF/ejb-jar.xml" " esi/eventMonitorActivationSpec jms/monQueue "" ]]]' )

AdminConfig.save()