package com.bertramlabs.payjunction

class TransactionBuilder {
	def params = [:]

	def TEST_CARD = [number: '4444333322221111', expiryMonth: '01', expiryYear: '2012', cvv: '999']

	def creditCard(opts=[number:'', expiryMonth:'', expiryYear:0, cvv:'']) {
		if(!opts.number) {
			throw new IllegalArgumentException("Credit card number required")
		}
		if(!opts.expiryMonth) {
			throw new IllegalArgumentException("Expiration month required")
		}
		if(!opts.expiryYear) {
			throw new IllegalArgumentException("Expiration year required")
		}
		if(!opts.cvv) {
			throw new IllegalArgumentException("CVV required")
		}

		def monthStr = "${opts.expiryMonth}"
		if(!((monthStr.length() == 2) && (monthStr ==~ /0?[1-9]|10|11|12/) )) {
			throw new IllegalArgumentException("Month must be two digits")
		}

		if(!("${opts.expiryYear}" ==~ /\d{4}/)) {
			throw new IllegalArgumentException("Year must be four digits")
		}

		params.cardNumber = opts.number
		params.cardExpMonth = opts.expiryMonth
		params.cardExpYear = opts.expiryYear
		params.cardCvv = opts.cvv
		params.cvv = 'ON'
	}

	def billingInfo(Closure c) {
		def clozure = c.clone()
		def billingContact = ContactBuilder.billing()
		clozure.delegate = billingContact
		clozure()
		params += billingContact.build()
	}

	def shippingInfo(Closure c) {
		def clozure = c.clone()
		def shippingContact = ContactBuilder.shipping()
		clozure.delegate = shippingContact
		clozure()
		params += shippingContact.build()
	}

	def charge(opts=[amount: 0, shipping:0, tip: 0, tax:0]) {
		if(opts.amount <= 0) {
			throw new IllegalArgumentException("Amount must be positive")
		}

		clearAmounts()
		setupTransactionAmounts(opts.amount, opts)
		params.action = 'CHARGE'
	}

	def refund(amt) {
		if(amt <= 0) {
			throw new IllegalArgumentException("Amount must be positive")
		}

		clearAmounts()
		setupTransactionAmounts(amt)
		params.action = 'REFUND'
	}

	/**
	 * Only used for transaction updates.
	 */
	def doVoid() {
		clearAmounts()
		params.status = 'VOID'
	}

	/**
	 * Only used for transaction updates.
	 */
	def capture(amt) {
		clearAmounts()
		setupTransactionAmounts(amt, opts)
		params.status = 'CAPTURE'
	}

	/**
	 * Only used for transaction updates.
	 */
	def hold(amt) {
		clearAmounts()
		setupTransactionAmounts(amt, opts)
		params.status = 'HOLD'
	}

	def invoice(invoiceNumber) {
		params.invoiceNumber = invoiceNumber
	}

	def purchaseOrder(poNumber) {
		params.purchaseOrderNumber = poNumber
	}

	def note(noteText) {
		params.note = noteText
	}

	def avs(Closure c) {
		def clozure = c.clone()
		def avs = new AvsConfig()
		clozure.delegate = avs
		clozure()

		params.avs = avs.build()
	}

	def build() {
		params + [:] // send back a copy
	}

	private setupTransactionAmounts(amt, opts=[shipping:0, tip: 0, tax:0]) {
		['shipping', 'tip', 'tax'].each {param ->
			if(opts[param]) {
				if(opts[param] < 0) {
					throw new IllegalArgumentException("${param.capitalize()} must be a positive value, if specified")
				} else {
					params["amount${param.capitalize()}"] = opts[param]
				}
			}
		}

		params.amountBase = amt
	}

	/**
	 * Call this to clear amounts for charges/refunds because only one of those can
	 * be performed in a transaction
	 */
	private clearAmounts() {
		['action', 'amountBase', 'amountTip', 'amountShipping', 'amountTax', 'amountReject'].each {
			params.remove(it)
		}
		params.remove('action')
		params.remove('status')
	}

}