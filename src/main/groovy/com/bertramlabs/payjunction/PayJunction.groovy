package com.bertramlabs.payjunction

class PayJunction {
	def server
	def userName
	def password
	def apiKey

	def getTransactions() {
		new TransactionRequest(buildRequest())
	}

	def transaction(int id) {
		def t = new TransactionRequest(buildRequest())
		t.getAt(id)
	}

	def getCustomers() {
		new CustomerRequest(buildRequest())
	}

	def customer(int id) {
		def c = new CustomerRequest(buildRequest())
		c.getAt(id)
	}

	def getAddresses() {
		new PayJunctionRequest(httpRequest: buildRequest(), url: '/trinity/api/address')
	}

	def address(int id) {
		def a = new PayJunctionRequest(httpRequest: buildRequest(), url: '/trinity/api/address')
		a.getAt(id)
	}

	def getVault() {
		new PayJunctionRequest(httpRequest: buildRequest(), url: '/trinity/api/vault')
	}

	def vault(int id) {
		def v = new PayJunctionRequest(httpRequest: buildRequest(), url: '/trinity/api/vault')
		v.getAt(id)
	}

	/**
	 * Creates an instance with test credentials.
	 */
	static def testInstance = {->
		new PayJunction(server: 'https://www.payjunctionlabs.com', userName: 'pj-ql-01', password: 'pj-ql-01p')
	}

	private buildRequest() {
		new HttpRequest(server:server, userName: userName, password: password, apiKey: apiKey)
	}

	private execClosure(Closure c, deleg) {
		def clozure = c.clone()
		clozure.delegate = deleg
		clozure()
	}
	
}