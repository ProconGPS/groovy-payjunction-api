package com.bertramlabs.payjunction

import groovy.json.JsonSlurper
import groovy.transform.InheritConstructors

@InheritConstructors
class PayJunctionException extends Exception {

	def getJson() {
		new JsonSlurper().parseText(message ?: '{}')
	}
}