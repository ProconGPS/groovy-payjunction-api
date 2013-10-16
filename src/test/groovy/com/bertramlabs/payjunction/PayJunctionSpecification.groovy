package com.bertramlabs.payjunction

import spock.lang.*

class PayJunctionSpecification extends Specification {
	def underTest

	def setup() {
		underTest = PayJunction.testInstance()
	}
}