package com.bertramlabs.payjunction

import spock.lang.*

class ContactBuilderSpecification extends Specification {
	def underTest

	def setup = {->
		underTest = new ContactBuilder()
	}

	def "Build billing contact"() {

	setup:
	underTest = ContactBuilder.billing()
	
	when:
	underTest.firstName 'A-Aron'

	then:
	underTest.contactMap.billingFirstName == 'A-Aron'
	}

	def "build shipping contact"() {

	setup:
	underTest = ContactBuilder.shipping()

	when:
	underTest.firstName 'A-Aron'

	then:
	underTest.contactMap.shippingFirstName == 'A-Aron'

	}

	def "throw error for invalid property"() {
	setup:
	underTest = ContactBuilder.shipping()

	when:
	underTest.madeUpProperty "Something"

	then:
	thrown(MissingMethodException)
	}
}