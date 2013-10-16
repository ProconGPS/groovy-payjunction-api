package com.bertramlabs.payjunction

import spock.lang.*
import static com.bertramlabs.payjunction.AvsConfig.*

class AvsConfigSpecification extends Specification {
	def underTest

	def setup() {
		underTest = new AvsConfig()
	}

	def "can specify address-only verification"() {
	when:
	underTest.verify address

	then:
	underTest.build() == 'ADDRESS'
	}

	def "can specify zip-only verification"() {
	when:
	underTest.verify zip

	then:
	underTest.build() == 'ZIP'
	}

	def "can specify address and zip verification"() {
	when:
	underTest.verify zip and address

	then:
	underTest.build() == 'ADDRESS_AND_ZIP'
	}

	def "can specify address OR zip verification"() {
	when:
	underTest.verify zip or address

	then:
	underTest.build() == 'ADDRESS_OR_ZIP'
	}

	def "can turn off AVS"() {
	when:
	underTest.off()

	then:
	underTest.build() == 'OFF'
	}

	def "can bypass AVS so if it fails the transaction will continue"() {
	when:
	underTest.bypass()

	then:
	underTest.build() == 'BYPASS'
	}


}