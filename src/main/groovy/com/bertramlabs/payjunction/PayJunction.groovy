package com.bertramlabs.payjunction

class PayJunction {
	def server
	def userName
	def password

	/**
	 * @param logon logon is the term used by PayJunction for the username.
	 * @param password the password for the account
	 */
	def newTransaction = {Closure c ->
		def builder = new TransactionBuilder()
		execClosure(c, builder)
		buildRequest().post('/trinity/api/transaction', builder.build())
	}

	def transaction = {id ->
		buildRequest().get("/trinity/api/transaction/${id}").response
	}

	def getTransactions() {
		buildRequest().get("/trinity/api/transaction/").results
	}

	def updateTransaction = {id, Closure c ->
		def builder = new TransactionBuilder()
		execClosure(c, builder)
		buildRequest().put("/trinity/api/transaction/${id}", builder.build())
	}

	def getRejects() {
		buildRequest().get("/trinity/api/transactions/rejects").results
	}

	/**
	 * Creates an instance with test credentials.
	 */
	static def testInstance = {->
		new PayJunction(server: 'https://www.payjunctionlabs.com', userName: 'pj-ql-01', password: 'pj-ql-01p')
	}

	private buildRequest() {
		new PayJunctionRequest(server:server, userName: userName, password: password)
	}

	private execClosure(Closure c, deleg) {
		def clozure = c.clone()
		clozure.delegate = deleg
		clozure()
	}
	
}