package com.bertramlabs.payjunction

import groovy.json.JsonSlurper

class TransactionReceiptRequest extends PayJunctionRequest {
	def validSubRequests = ['unsignedThermal', 'unsignedFullpage', 'sign']

	def getUnsignedThermal() {
		checkAllowed('unsignedThermal')
		returnCopy("${url}/unsigned/thermal")
	}

	def getUnsignedFullpage() {
		checkAllowed('unsignedFullpage')
		returnCopy("${url}/unsigned/fullpage")
	}

	def getSignature() {
		checkAllowed('signature')
		returnCopy("${url}/signature")
	}

	def getThermal() {
		checkAllowed('thermal')
		returnCopy("${url}/thermal")
	}

	def getFullpage() {
		checkAllowed('fullpage')
		returnCopy("${url}/fullpage")
	}

	def sign() {
		checkAllowed('sign')
		throw new UnsupportedOperationException()
	}

	def email() {
		checkAllowed('email')
		throw new UnsupportedOperationException()
	}

	@Override
	def getAt(int idx) {
		def c = super.getAt(idx)
		c.validSubRequests = ['signature', 'thermal', 'fullpage', 'email']
		c.idAllowed = false
		c
	}

	@Override
	def clone() {
		new TransactionReceiptRequest(httpRequest: httpRequest, url: url, context: context, idAllowed: idAllowed, _offset: _offset)
	}

	private checkAllowed(name) {
		if(!(name in validSubRequests)) {
			throw new IllegalStateException("Cannot not make '${name}' request for URL '${url}'");
		}
	}

	private returnCopy(newUrl) {
		def c = this.clone()
		c.validSubRequests = []
		c.idAllowed = false
		c.url = newUrl
		c
	}
}
