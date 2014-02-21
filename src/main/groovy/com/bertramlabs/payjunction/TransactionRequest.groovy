package com.bertramlabs.payjunction

import groovy.json.JsonSlurper

class TransactionRequest extends PayJunctionRequest {
	def validSubRequests = ['rejects']

	public TransactionRequest(HttpRequest req) {
		httpRequest = req
		url = '/transaction'
		context = new TransactionBuilder()
	}

	def getReceipts() {
		if(!('receipts' in validSubRequests)) {
			throw new IllegalStateException("Cannot perform 'receipts' request for url ${url}")
		}
		new TransactionReceiptRequest(httpRequest: httpRequest, url: "${url}/receipt", context: context, idAllowed: true, _offset: _offset)
	}

	def getRejects() {
		if(!('rejects' in validSubRequests)) {
			throw new IllegalStateException("Cannot perform 'rejects' request for url ${url}")
		}
		new CustomerVaultRequest(httpRequest: httpRequest, url: "${url}/rejects", context: context, idAllowed: false, _offset: _offset)
	}
	
	@Override
	def getAt(int idx) {
		def c = super.getAt(idx)
		c.validSubRequests = ['receipts', 'rejects']
		c
	}

	@Override
	def clone() {
		def c = new TransactionRequest(httpRequest)
		c.url = url
		c.context = context
		c.idAllowed = idAllowed
		c._offset = _offset
		c
	}
}
