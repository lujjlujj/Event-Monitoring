#Delete the DefProperty
for defProperty in AdminConfig.getid('/Cell:/DefListenerConfig:/DefListener:/DefProperty:/').splitlines()
	AdminConifg.remove(defProperty)
print('Delete DefProperty Successfully')

#Delete the DefFilter
for defFilter in AdminConfig.getid('/Cell:/DefListenerConfig:/DefListener:/DefFilter:/').splitlines()
	AdminConfig.remove(defFilter)
print('Delete DefFilter Successfully')

#Delete the DefListener
for defListener in AdminConfig.getid('/Cell:/DefListenerConfig:/DefListener:/').splitlines()
	AdminConfig.remove(defListener)
print('Delete DefListener Successfully')

#Delete the DeflistenerConfig
for defListenerConfig in AdminConfig.getid('/Cell:/DefListenerConfig:/').splitlines()
	AdminConfig.remove(defListenerConfig)
print('Delete DefListenerConfig Successfully')

#Delete the DefProducer DefFilter
for defFilter in AdminConfig.getid('/Cell:/DefProducerConfig:/DefProducer:/DefFilter:/').splitlines()
	AdminConfig.remove(defFilter)
print('Delete DefProducer DefFilter Successfully')

#Delete the DefProducer
for defProducer in AdminConfig.getid('/Cell:/DefProducerConfig:/DefProducer:/').splitlines()
	AdminConfig.remove(defProducer)
print('Delete DefProducer Successfully')

#Delete the DefProducerConfig
for defProducerConfig in AdminConfig.getid('/Cell:/DefProducerConfig:/').splitlines()
	AdminConfig.remove(defProducerConfig)
print('Delete DefProducerConfig Successfully')

AdminConfig.save()