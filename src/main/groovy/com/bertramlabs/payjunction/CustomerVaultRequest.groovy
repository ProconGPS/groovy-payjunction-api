package com.bertramlabs.payjunction

import groovy.json.JsonSlurper

class CustomerVaultRequest extends PayJunctionRequest {
	public CustomerVaultRequest(HttpRequest req, String urlRoot, int offset) {
		httpRequest = req
		_offset = offset
		idAllowed = true
		url = "${urlRoot}/vault"
		context = new EntityBuilder(allowedParams: [
																	'cardNumber',
																	'cardExpMonth',
																	'cardExpYear',
																	'address',
																	'city',
																	'state',
																	'zip',
																	'addressId',
																	'achRoutingNumber',
																	'achAccountNumber',
																	'achAccountType',
																	'achType'])
																	
	}

	@Override
	def getAt(int idx) {
		def c = super.getAt(idx)
		c
	}

	@Override
	def clone() {
		def c = new CustomerVaultRequest(httpRequest, '', _offset)
		c.url = url
		c.context = context
		c.idAllowed = idAllowed
		c._offset = _offset
		c
	}
}
