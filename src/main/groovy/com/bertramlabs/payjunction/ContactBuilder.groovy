package com.bertramlabs.payjunction

class ContactBuilder {
	def contactMap = [:]
	def prefix
	static final validProps = ['identifier', 'firstName', 'middleName', 'lastName', 'companyName', 'jobTitle',
														 'phone', 'phone2', 'address', 'city', 'state', 'zip', 'country', 'email', 'website'] as Set;

	static shipping() {
		new ContactBuilder(prefix: 'shipping')
	}

	static billing() {
		new ContactBuilder(prefix: 'billing')
	}

	def methodMissing(String name, args) {
		if((name in validProps) && (args.size() == 1)) {
			return contactMap["${prefix}${name.capitalize()}"] = args[0]
		} else {
			throw new MissingMethodException(name, String.class, args)
		}
	}

	def build() {
println "contactMap : $contactMap"
		contactMap + [:] // return a copy
	}
}
