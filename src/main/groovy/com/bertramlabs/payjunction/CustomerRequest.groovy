package com.bertramlabs.payjunction

import groovy.json.JsonSlurper

class CustomerRequest extends PayJunctionRequest {
	def validSubRequests = []

	public CustomerRequest(HttpRequest req) {
		httpRequest = req
		url = '/customers'
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

	def getAddresses() {
		if(!('addresses' in validSubRequests)) {
			throw new IllegalStateException("Cannot perform 'address' request for url ${url}")
		}
		new CustomerAddressRequest(httpRequest: httpRequest, url: "${url}/addresses", context: context, idAllowed: false, _offset: _offset)
	}

	def getVaults() {
		if(!('vaults' in validSubRequests)) {
			throw new IllegalStateException("Cannot perform 'vaults' request for url ${url}")
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
		c.validSubRequests = ['addresses', 'vaults']
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
