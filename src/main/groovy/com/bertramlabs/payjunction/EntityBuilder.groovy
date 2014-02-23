package com.bertramlabs.payjunction

class EntityBuilder {
	def params = [:]
	def allowedParams = []


	def methodMissing(String name, args) {
		if(args.size() == 0) {
			return params[name]
		} else if (args.size() == 1) {
			params[name] = args[0]
		} else {
			throw new MissingMethodException(name, delegate, args)
		}
	}

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