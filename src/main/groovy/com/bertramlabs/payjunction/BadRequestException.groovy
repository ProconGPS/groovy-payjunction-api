package com.bertramlabs.payjunction

import groovy.transform.InheritConstructors

class BadRequestException extends PayJunctionException {
	def errors
}