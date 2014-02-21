package com.bertramlabs.payjunction

class EntityBuilder {
	def params = [:]
	def allowedParams = []
												
	def propertyMissing(String name, val) {
		if(name in allowedParams) {
			params[name] = val
		} else {
			throw new MissingPropertyException(name, val.class)
		}
	}
	
	def propertyMissing(String name) {
		if(name in allowedParams) {
			return params[name]
		} else {
			throw new MissingPropertyException("${name} does not exist")
		}
	}

	def build() {
		params + [:]
	}
}