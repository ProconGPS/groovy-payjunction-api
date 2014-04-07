package com.bertramlabs.payjunction

import spock.lang.*

class TransactionBuilderSpecification extends Specification {
	def underTest

	def setup() {
		underTest = new TransactionBuilder()
	}

	def "credit card number required"() {
	when:
	underTest.creditCard expiryMonth: '01', expiryYear: '2014', cvv: '123'

	then:
	thrown(IllegalArgumentException)
	}

	def "expiration month required"() {
	when:
	underTest.creditCard number: '4444444444444444', expiryYear: '2014', cvv: '123'

	then:
	thrown(IllegalArgumentException)
	}

	def "expiration year required"() {
	when:
	underTest.creditCard number: '4444444444444444', expiryMonth: '01', cvv: '123'

	then:
	thrown(IllegalArgumentException)
	}

	def "CVV required"() {
	when:
	underTest.creditCard number: '4444444444444444', expiryMonth: '01', expiryYear: '2014'

	then:
	thrown(IllegalArgumentException)
	}


	def "expiration month must be two digits"() {
	when:
	underTest.creditCard number: '4444444444444444', expiryMonth: '1', expiryYear: '2013', cvv: '123'

	then:
	thrown(IllegalArgumentException)
	}

	def "expiration year must be four digits"() {
	when:
	underTest.creditCard number: '4444444444444444', expiryMonth: '01', expiryYear: '13', cvv: '123'

	then:
	thrown(IllegalArgumentException)
	}

	def "can specify billing info"() {
	when:
	underTest.billingInfo {
		firstName "A-Aron"
		lastName "Aron"
		phone "3332221111"
		email "something@somewhere.com"
	}

	then:
	underTest.params.billingFirstName == 'A-Aron'
	underTest.params.billingLastName == 'Aron'
	underTest.params.billingPhone == '3332221111'
	underTest.params.billingEmail == 'something@somewhere.com'

	}

	def "can specify shipping info"() {
	when:
	underTest.shippingInfo {
		firstName "A-Aron"
		lastName "Aron"
		phone "3332221111"
		email "something@somewhere.com"
	}

	then:
	underTest.params.shippingFirstName == 'A-Aron'
	underTest.params.shippingLastName == 'Aron'
	underTest.params.shippingPhone == '3332221111'
	underTest.params.shippingEmail == 'something@somewhere.com'

	}

	def "can charge a specified amount"() {
	when:
	underTest.charge(amt: 1.00)

	then:
	underTest.params.amountBase == 1.0
	}

	def "amount must be positive"() {
	when:
	underTest.charge(amt: -1)

	then:
	thrown(IllegalArgumentException)
	}

	def "can specify separate shipping, tip, and tax"() {
	when:
	underTest.charge(amt:10.12, shipping: 2.23, tip: 1.23, tax: 2.30)

	then:
	underTest.params.amountBase == 10.12
	underTest.params.amountShipping == 2.23
	underTest.params.amountTip == 1.23
	underTest.params.amountTax == 2.3
	underTest.params.action == 'CHARGE'
	}

	def "can refund"() {
	when:
	underTest.refund(amt:10.12)

	then:
	underTest.params.amountBase == 10.12
	underTest.params.action == 'REFUND'
	}

	def "can specify AVS config"() {
	when:
	underTest.avs {
		verify address and zip
	}

	then:
	underTest.params.avs == 'ADDRESS_AND_ZIP'
	}

	def "can turn off AVS config"() {
	when:
	underTest.avs() {
		off()
	}

	then:
	underTest.params.avs == 'OFF'
	}
}