package com.bertramlabs.payjunction

import spock.lang.*

class PayJunctionRequestSpecification extends Specification {
	def pj
	def underTest

	def setup() {
		pj = PayJunction.testInstance()
		underTest = new PayJunctionRequest(server: pj.server, userName: pj.userName, password: pj.password)
	}

	def "can encode transactions correctly"() {
		def trans = new TransactionBuilder()
		trans.creditCard number: '33333333', expiryMonth: '02', expiryYear: '2014', cvv: '123'
		def formStr
	when:
	formStr = underTest.buildUrlEncodedFormData(trans.build())

	then:
	formStr.contains('cardNumber=33333333')
	formStr.contains('cardExpMonth=02')
	formStr.contains('cardExpYear=2014')
	formStr.contains('cardCvv=123')
	
	}
}