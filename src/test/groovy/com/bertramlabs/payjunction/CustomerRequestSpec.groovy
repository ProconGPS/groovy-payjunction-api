package com.bertramlabs.payjunction

import spock.lang.*

class CustomerRequestSpec extends Specification {
	def underTest
	def httpMock
	final createResponse = '''{"customerId":10079,"firstName":"jeremy","lastName":"crosbie","companyName":"spireon","email":"","phone":"800-601-0230","phone2":"800-771-3821","jobTitle":"Poet","website":"https://www.payjunction.com","custom1":"custom-field","created":"2012-02-09T02:12:14Z","lastModified":"2012-02-09T02:12:14Z"}'''

	def setup() {
		httpMock = Mock(HttpRequest)
		underTest = new CustomerRequest(httpMock)
	}

	def "can get a customer by Id"() {
		when:
		def result = underTest[12]
	
		then:
		result.url.contains('/12')
	}

	def "cannot get an address request info without a customer Id"() {
		when:
		def result = underTest.addresses
	
		then:
		thrown(IllegalStateException)
	}

	def "cannot get a vault info request without a customer Id"() {
		when:
		def result = underTest.vaults
	
		then:
		thrown(IllegalStateException)
	}

	def "can get an address request"() {
		when:
		def result = underTest[11].addresses
	
		then:
		result.url.endsWith('/11/addresses')
	}

	def "can get a vault info request"() {
		when:
		def result = underTest[23].vaults
	
		then:
		result.url.endsWith('/23/vaults')
	}

	def "can create a vault"() {
		when:
		httpMock.post(_,_) >> '{}'
		def result = underTest[23].vaults
		result.create {
			cardNumber = '123123123'
			cardExpMonth = '02'
			cardExpYear = '2022'
		}

		then:
		result.context.cardNumber == '123123123'
		result.context.cardExpMonth == '02'
		result.context.cardExpYear == '2022'
	}

	def "can only use vault-specific parameters during creation"() {
		when:
		httpMock.post(_,_) >> '{}'
		def result = underTest[23].vaults
		result.create {
			cardNumber = '123123123'
			cardExpMonth = '02'
			cardExpYear = '2022'
			bla = 'random'
		}

		then:
		thrown(MissingPropertyException)
	}


	def "can create a customer"() {
		when:
		1 * httpMock.post(_, _) >> createResponse
		def result = underTest.create {
			firstName = 'jeremy'
			lastName = 'crosbie'
			companyName = 'spireon'
		}
	
		then:
		result.customerId != null
		result.firstName == 'jeremy'
		result.lastName == 'crosbie'
		result.companyName == 'spireon'
	}
}



