package com.bertramlabs.payjunction

import groovy.json.JsonSlurper

class CustomerVaultRequest extends PayJunctionRequest {
	public CustomerVaultRequest(HttpRequest req, String urlRoot, int offset) {
		httpRequest = req
		_offset = offset
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
}
