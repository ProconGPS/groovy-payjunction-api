package com.bertramlabs.payjunction

import groovy.json.JsonSlurper

class CustomerRequest extends PayJunctionRequest {
	def validSubRequests = []

	public CustomerRequest(HttpRequest req) {
		httpRequest = req
		url = '/customer'
		context = new EntityBuilder(allowedParams: (['firstName',
																								 'lastName',
																								 'identifier',
																								 'companyName',
																								 'email',
																								 'phone',
																								 'phone2',
																								 'jobTitle',
																								 'website',
																								 'custom1'] as Set))
	}

	def getAddress() {
		if(!('address' in validSubRequests)) {
			throw new IllegalStateException("Cannot perform 'address' request for url ${url}")
		}
		new CustomerAddressRequest(httpRequest: httpRequest, url: "${url}/address", context: context, idAllowed: false, _offset: _offset)
	}

	def getVaults() {
		if(!('vault' in validSubRequests)) {
			throw new IllegalStateException("Cannot perform 'address' request for url ${url}")
		}
		new CustomerVaultRequest(httpRequest, url, _offset)
	}

	def vault(id) {
		if(!('vault' in validSubRequests)) {
			throw new IllegalStateException("Cannot perform 'address' request for url ${url}")
		}
		def req = new CustomerVaultRequest(httpRequest, url, _offset)
		req[id]
	}
	
	@Override
	def getAt(int idx) {
		def c = super.getAt(idx)
		c.validSubRequests = ['address', 'vault']
		c
	}

	@Override
	def clone() {
		def c = new CustomerRequest(httpRequest)
		c.url = url
		c.context = context
		c.idAllowed = idAllowed
		c._offset = _offset
		c
	}
}
