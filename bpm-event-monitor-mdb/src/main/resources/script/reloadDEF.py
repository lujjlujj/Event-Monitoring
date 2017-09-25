dmList = AdminControl.queryNames('type=DefManagement,*').splitlines()
AdminControl.invoke(dmlist[0], 'reloadConfiguredEventListeners')

AdminConfig.save()